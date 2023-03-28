package com.hhvvg.launcher

import android.content.res.Resources
import android.content.res.XModuleResources
import com.hhvvg.launcher.service.LauncherService
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage

/**
 * @author hhvvg
 */
class Init : IXposedHookLoadPackage, IXposedHookZygoteInit{
    private val launcherProvider by lazy {
        LauncherHookProvider()
    }

    override fun handleLoadPackage(param: XC_LoadPackage.LoadPackageParam) {
        val pkg = param.packageName
        if ("com.hhvvg.launcher3customizer" == pkg) {
            return
        }
        if ("android" == param.packageName) {
            // Init ILauncherService
            LauncherService.initLauncherService()
        }
        classLoader = param.classLoader
        if ("android" != param.packageName) {
            launcherProvider.init(param)
        }
    }

    override fun initZygote(param: IXposedHookZygoteInit.StartupParam) {
        modulePath = param.modulePath
        moduleRes = createModuleRes(modulePath)
    }

    companion object {
        @JvmStatic
        lateinit var classLoader: ClassLoader

        @JvmStatic
        lateinit var moduleRes: Resources

        @JvmStatic
        lateinit var modulePath: String

        @JvmStatic
        fun createModuleRes(path: String): Resources {
            return XModuleResources.createInstance(path, null)
        }
    }
}
