package com.hhvvg.launcher

import android.app.AndroidAppHelper
import android.app.Application
import com.hhvvg.launcher.component.Inject
import com.hhvvg.launcher.component.LauncherComponent
import com.hhvvg.launcher.component.LauncherMethod
import com.hhvvg.launcher.utils.Logger
import com.hhvvg.launcher.utils.launcher
import de.robv.android.xposed.XC_MethodHook.MethodHookParam

/**
 * @author hhvvg
 */
class LauncherApplication : LauncherComponent() {
    override val className: String
        get() = "android.app.Application"

    @LauncherMethod(inject = Inject.After)
    fun override_onCreate(params: MethodHookParam) {
        initApplication(AndroidAppHelper.currentApplication())
    }

    companion object {
        const val LAUNCHER = "Launcher"

        private fun initApplication(app: Application) {
            app.launcher = Launcher()
        }

        @JvmStatic
        fun setLauncher(launcher: Launcher) {
            AndroidAppHelper.currentApplication().launcher = launcher
        }
    }
}