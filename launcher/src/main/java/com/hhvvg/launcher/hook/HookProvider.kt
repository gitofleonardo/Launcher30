package com.hhvvg.launcher.hook

import com.hhvvg.launcher.Init
import com.hhvvg.launcher.component.Component
import com.hhvvg.launcher.component.LauncherComponent
import com.hhvvg.launcher.component.LauncherMethod
import com.hhvvg.launcher.component.MethodInjection
import com.hhvvg.launcher.utils.Logger
import com.hhvvg.launcher.utils.getAdditionalInstanceField
import com.hhvvg.launcher.utils.setAdditionalInstanceField
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * Initialized from {@link de.robv.android.xposed.IXposedHookLoadPackage#handleLoadPackage}.
 */
fun init() {
    for (cn in components) {
        for (method in cn.getLauncherMethods()) {
            val methodEntry = method.parseLauncherMethod(cn) ?: continue
            methodEntry.applyMethodHook()
        }
    }
}

/**
 * Retrieve all annotated methods, which will be hooked later.
 */
fun KClass<*>.getLauncherMethods(): Array<Method> {
    val methods = java.declaredMethods
    return methods
        .filter {
            it.isAnnotationPresent(LauncherMethod::class.java)
        }
        .toTypedArray()
}

/**
 * Parse an annotated method.
 */
fun <T : Component> Method.parseLauncherMethod(component: KClass<T>): LauncherMethodEntry<T>? {
    return try {
        val launcherClass = component.getLauncherClass()
        val injections = parseInjections()
        val argTypes = parseLauncherMethodArgTypes()
        val launcherMethodName = parseLauncherMethodName()
        val isConstructor = launcherMethodName == "constructor"

        val entry = LauncherMethodEntry(
            component = component,
            launcherClass = launcherClass,
            method = this,
            launcherMethodName = launcherMethodName,
            isConstructor = isConstructor,
            methodInjection = injections.toSet(),
            methodArgTypes = argTypes
        )
        entry
    } catch (e : Exception) {
        Logger.log(e.toString())
        null
    }
}

/**
 * Get specified launcher class, for which this class stands.
 */
fun KClass<*>.getLauncherClass(): KClass<*> {
    val component = java.annotations
        .filterIsInstance(LauncherComponent::class.java)
        .first()
    val launcherClassName = component.className
    return XposedHelpers.findClass(launcherClassName, Init.classLoader).kotlin
}

/**
 * Get specified launcher class, for which this class stands.
 */
fun Class<*>.getLauncherClass(): KClass<*> {
    return kotlin.getLauncherClass()
}

/**
 * Get specified launcher java class, for which this class stands.
 */
fun Class<*>.getLauncherJavaClass(): Class<*> {
    return getLauncherClass().java
}

/**
 * Parse launcher method name from module method name. Module method name often equals with the
 * launcher name, but sometimes it starts with {@code _override} to avoid ambiguity.
 */
private fun Method.parseLauncherMethodName(): String {
    val thisMethodName = name
    if (thisMethodName.startsWith("override_")) {
        return thisMethodName.substringAfter("override_")
    }
    return thisMethodName
}

/**
 * Parse method injection position. By default, module method runs after launcher method.
 */
private fun Method.parseInjections(): Array<MethodInjection> {
    val launcherMethodAnnotation = getAnnotation(LauncherMethod::class.java)
        ?: throw IllegalArgumentException("Method must be annotated with LauncherMethod")
    val injections = launcherMethodAnnotation.injections.toHashSet()

    if (injections.isEmpty()) {
        injections.add(MethodInjection.After)
    }

    return injections.toTypedArray()
}

/**
 * Parse module method argument types.
 */
private fun Method.parseLauncherMethodArgTypes(): List<KClass<*>> {
    val result = ArrayList<KClass<*>>()

    for (i in parameters.indices) {
        val param = parameters[i]
        val paramType = param.type

        result.add(paramType.kotlin)
    }
    return result
}

/**
 * Apply module method hook.
 */
private fun LauncherMethodEntry<*>.applyMethodHook() {
    try {
        val startsWithHookParam = methodArgTypes.isNotEmpty() &&
                methodArgTypes[0].java.isAssignableFrom(MethodHookParam::class.java)
        var methodParams = methodArgTypes
            .map {
                if (Component::class.java.isAssignableFrom(it.java)) {
                    it.getLauncherClass().java
                } else {
                    it.java
                }
            }
            .toTypedArray()
        if (startsWithHookParam) {
            methodParams = methodParams.copyOfRange(1, methodArgTypes.size)
        }

        val callback: (param: MethodHookParam) -> Unit = { param ->
            val realArgs = ArrayList<Any?>()

            for (i in methodArgTypes.indices) {
                if (i == 0 && startsWithHookParam) {
                    realArgs.add(param)
                    continue
                }

                val type = methodArgTypes[i]

                val realArgIndex = if (startsWithHookParam) {
                    i - 1
                } else {
                    i
                }
                if (Component::class.java.isAssignableFrom(type.java)) {
                    val component: Component = if (param.thisObject == null) {
                        // Is a static method
                        type.java.newInstance() as Component
                    } else {
                        param.getCachedComponent(type, launcherClass.java.simpleName)
                    }

                    val realArg = param.args[realArgIndex]
                    component.instance = realArg

                    realArgs.add(component)
                    continue
                }

                realArgs.add(param.args[realArgIndex])
            }

            val realComponent = component.java.newInstance()
            realComponent.instance = param.thisObject
            method.isAccessible = true
            method.invoke(realComponent, *realArgs.toTypedArray())
        }

        val methodHook = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                if (methodInjection.contains(MethodInjection.Before)) {
                    callback.invoke(param)
                }
            }

            override fun afterHookedMethod(param: MethodHookParam) {
                if (methodInjection.contains(MethodInjection.After)) {
                    callback.invoke(param)
                }
            }
        }

        if (isConstructor) {
            XposedHelpers.findAndHookConstructor(launcherClass.java, *methodParams, methodHook)
            return
        }

        val launcherMethod = XposedHelpers.findMethodBestMatch(launcherClass.java, launcherMethodName, *methodParams)
        XposedBridge.hookMethod(launcherMethod, methodHook)
    } catch (e : Exception) {
        Logger.log("Failed to hook method: ${this.launcherMethodName}")
    }
}

/**
 * Get cached component from launcher instance. Ensure this method be called within a non-static
 * method.
 */
private fun MethodHookParam.getCachedComponent(type: KClass<*>, className: String): Component {
    val instanceName = "_component_cached_instance_${className}"
    var cachedComponent = thisObject.getAdditionalInstanceField<Component>(instanceName)

    if (cachedComponent == null) {
        cachedComponent = type.java.newInstance() as Component
        thisObject.setAdditionalInstanceField(instanceName, cachedComponent)
    }

    return cachedComponent
}