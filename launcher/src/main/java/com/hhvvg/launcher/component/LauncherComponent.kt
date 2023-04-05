package com.hhvvg.launcher.component

abstract class LauncherComponent {
    abstract val className: String
    var instance: Any? = null
}
