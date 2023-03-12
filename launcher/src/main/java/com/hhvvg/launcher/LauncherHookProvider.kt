package com.hhvvg.launcher

import com.hhvvg.launcher.component.Inject
import com.hhvvg.launcher.component.LauncherArgs
import com.hhvvg.launcher.component.LauncherComponent
import com.hhvvg.launcher.component.LauncherMethod
import com.hhvvg.launcher.folder.FolderIcon
import com.hhvvg.launcher.folder.PreviewBackground
import com.hhvvg.launcher.icon.BubbleTextView
import com.hhvvg.launcher.icon.FastBitmapDrawable
import com.hhvvg.launcher.icon.IconCache
import com.hhvvg.launcher.icon.LauncherActivityCachingLogic
import com.hhvvg.launcher.utils.Logger
import com.hhvvg.launcher.utils.getAdditionalInstanceField
import com.hhvvg.launcher.utils.setAdditionalInstanceField
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XC_MethodHook.MethodHookParam
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.reflect.Method

/**
 * @author hhvvg
 */
class LauncherHookProvider {
    private val components: Array<Class<*>> = arrayOf(
        LauncherApplication::class.java,
        Launcher::class.java,
        DeviceProfile::class.java,
        FastBitmapDrawable::class.java,
        IconCache::class.java,
        LauncherActivityCachingLogic::class.java,
        BubbleTextView::class.java,
        FolderIcon::class.java,
        PreviewBackground::class.java
    )

    fun init(param: XC_LoadPackage.LoadPackageParam) {
        for (componentClass in components) {
            val c = componentClass.newInstance() as LauncherComponent
            val classProcessor = ClassProcessor(c)
            for (method in classProcessor.annotatedMethods) {
                val methodProcessor = MethodProcessor(method, param.classLoader, c)
                if (!methodProcessor.validMethod) {
                    continue
                }
                MethodHook(c, param.classLoader, methodProcessor)
            }
        }
    }
}

private class ClassProcessor(val component: LauncherComponent) {
    val annotatedMethods: Array<Method> = findMethods()
    private fun findMethods(): Array<Method> {
        Logger.log("Hook class: ${component.className}")

        val methods = component::class.java.declaredMethods
        return methods
            .filter {it.isAnnotationPresent(LauncherMethod::class.java)}
            .toTypedArray()
    }
}

private class MethodProcessor(
    val method: Method,
    val classLoader: ClassLoader,
    val component: LauncherComponent
) {
    var validMethod: Boolean = false
    lateinit var launcherClassName: String
    lateinit var launcherMethodName: String
    lateinit var launcherMethodInject: Inject
    lateinit var launcherMethodTypes: Array<Class<*>>

    init {
        process()
    }

    fun process() {
        try {
            launcherClassName = component.className
            launcherMethodName = parseMethodName()
            launcherMethodInject = parseMethodPoint()
            launcherMethodTypes = parseMethodTypes()
            validMethod = true
        } catch (e: Exception) {
            validMethod = false
            Logger.log("Failed to parse method: ${method.name}", e)
        }
    }

    private fun parseMethodName(): String {
        val methodName = method.name
        if (methodName.equals("constructor")) {
            return "constructor"
        } else if (!methodName.startsWith("override_")) {
            throw IllegalArgumentException("LauncherMethod must be started with override_")
        }
        return methodName.substringAfter("override_")
    }

    private fun parseMethodPoint(): Inject {
        val launcherMethod: LauncherMethod = method.getDeclaredAnnotation(LauncherMethod::class.java)!!
        return launcherMethod.inject
    }

    private fun parseMethodTypes(): Array<Class<*>> {
        val result = ArrayList<Class<*>>()

        for (i in 0 until method.parameters.size) {
            val param = method.parameters[i]
            val paramType = param.type

            if (i == 0 && !paramType.isAssignableFrom(MethodHookParam::class.java)) {
                throw IllegalArgumentException("First param of hooked method must be MethodHookParam")
            }

            if (!paramType.isAssignableFrom(LauncherComponent::class.java)) {
                result.add(paramType)
                continue
            }

            if (!param.isAnnotationPresent(LauncherArgs::class.java)) {
                throw IllegalArgumentException("LauncherComponent must be annotated with LauncherArgs")
            }

            val launcherArg = param.getDeclaredAnnotation(LauncherArgs::class.java)!!
            val className = launcherArg.className
            val clz = classLoader.loadClass(className)
            result.add(clz)
        }
        return result.toTypedArray()
    }
}

private class MethodHook(
    val component: LauncherComponent,
    val classLoader: ClassLoader,
    val methodProcessor: MethodProcessor
) {
    init {
        try {
            hookMethod()
        } catch (e : Exception) {
            Logger.log("Error hooking method: ${methodProcessor.launcherMethodName}", e)
        }
    }

    fun hookMethod() {
        val launcherClass = XposedHelpers.findClass(methodProcessor.launcherClassName, classLoader)
        val launcherMethodParams = methodProcessor.launcherMethodTypes
            .copyOfRange(1, methodProcessor.launcherMethodTypes.size)

        val callback: (param: MethodHookParam) -> Unit =  { p ->
            val realArgs = ArrayList<Any>()
            methodProcessor.launcherMethodTypes.forEachIndexed { i, c ->
                if (i == 0) {
                    realArgs.add(p)
                } else if (c.isAssignableFrom(LauncherComponent::class.java)) {
                    val instanceName = "_component_${component.javaClass.simpleName}"
                    var cachedComponent = p.thisObject.getAdditionalInstanceField<LauncherComponent>(instanceName)
                    if (cachedComponent == null) {
                        cachedComponent = c.newInstance() as LauncherComponent
                        p.thisObject.setAdditionalInstanceField(instanceName, cachedComponent)
                    }
                    val realArg =cachedComponent
                    val arg = p.args[i]
                    realArg.instance = arg
                    realArgs.add(realArg)
                } else {
                    realArgs.add(p.args[i - 1])
                }
            }
            val realComponent = component::class.java.newInstance()
            realComponent.instance = p.thisObject
            methodProcessor.method.invoke(realComponent, *realArgs.toTypedArray())
        }
        val methodHook = when(methodProcessor.launcherMethodInject) {
            Inject.Before -> object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    callback.invoke(param)
                }
            }
            Inject.After -> object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    callback.invoke(param)
                }
            }
        }
        if (methodProcessor.method.name.equals("constructor")) {
            XposedHelpers.findAndHookConstructor(launcherClass, *launcherMethodParams, methodHook)
        } else {
            val launcherMethod = XposedHelpers.findMethodBestMatch(launcherClass,
                methodProcessor.launcherMethodName, *launcherMethodParams)
            XposedBridge.hookMethod(launcherMethod, methodHook)
        }
        Logger.log("Hook method: ${methodProcessor.launcherMethodName}")
    }
}