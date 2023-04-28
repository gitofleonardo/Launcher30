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

fun init() {
    for (cn in components) {
        for (method in cn.getLauncherMethods()) {
            val methodEntry = method.parseLauncherMethod(cn) ?: continue
            methodEntry.applyMethodHook()
        }
    }
}

fun KClass<*>.getLauncherMethods(): Array<Method> {
    val methods = java.declaredMethods
    return methods
        .filter {
            it.isAnnotationPresent(LauncherMethod::class.java)
        }
        .toTypedArray()
}

fun <T : Component> Method.parseLauncherMethod(component: KClass<T>): LauncherMethodEntry<T>? {
    return try {
        val launcherClass = component.getLauncherClass()
        val injections = name.parseInjections()
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

fun KClass<*>.getLauncherClass(): KClass<*> {
    val component = java.annotations
        .filterIsInstance(LauncherComponent::class.java)
        .first()
    val launcherClassName = component.className
    return XposedHelpers.findClass(launcherClassName, Init.classLoader).kotlin
}

fun Class<*>.getLauncherClass(): KClass<*> {
    return kotlin.getLauncherClass()
}

fun Class<*>.getLauncherJavaClass(): Class<*> {
    return getLauncherClass().java
}

private fun Method.parseLauncherMethodName(): String {
    val methodName = name
    return methodName.substringAfter('$')
}

private fun String.parseInjections(): Array<MethodInjection> {
    val methodSplit = substringBefore('$').split('_')
    val injections = HashSet<MethodInjection>()

    for (element in methodSplit) {
        if ("before" == element) {
            injections.add(MethodInjection.Before)
        } else if ("after" == element) {
            injections.add(MethodInjection.After)
        }
    }

    if (injections.isEmpty()) {
        injections.add(MethodInjection.After)
    }

    return injections.toTypedArray()
}

private fun Method.parseLauncherMethodArgTypes(): List<KClass<*>> {
    val result = ArrayList<KClass<*>>()

    for (i in parameters.indices) {
        val param = parameters[i]
        val paramType = param.type

        if (i == 0 && !paramType.isAssignableFrom(MethodHookParam::class.java)) {
            throw IllegalArgumentException("First param of hooked method must be MethodHookParam")
        }

        result.add(paramType.kotlin)
    }
    return result
}

private fun LauncherMethodEntry<*>.applyMethodHook() {
    try {
        val methodParams = methodArgTypes
            .map {
                return@map if (Component::class.java.isAssignableFrom(it.java)) {
                    it.getLauncherClass().java
                } else {
                    it.java
                }
            }
            .toTypedArray()
            .copyOfRange(1, methodArgTypes.size)

        val callback: (param: MethodHookParam) -> Unit = { param ->
            val realArgs = ArrayList<Any?>()

            for (i in methodArgTypes.indices) {
                if (i == 0) {
                    realArgs.add(param)
                    continue
                }

                val type = methodArgTypes[i]

                if (Component::class.java.isAssignableFrom(type.java)) {
                    val component: Component = if (param.thisObject == null) {
                        // Is a static method
                        type.java.newInstance() as Component
                    } else {
                        param.getCachedComponent(type, launcherClass.java.simpleName)
                    }

                    val realArg = param.args[i - 1]
                    component.instance = realArg

                    realArgs.add(component)
                    continue
                }

                realArgs.add(param.args[i - 1])
            }

            val realComponent = component.java.newInstance()
            realComponent.instance = param.thisObject
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

private fun MethodHookParam.getCachedComponent(type: KClass<*>, className: String): Component {
    val instanceName = "_component_cached_instance_${className}"
    var cachedComponent = thisObject.getAdditionalInstanceField<Component>(instanceName)

    if (cachedComponent == null) {
        cachedComponent = type.java.newInstance() as Component
        thisObject.setAdditionalInstanceField(instanceName, cachedComponent)
    }

    return cachedComponent
}