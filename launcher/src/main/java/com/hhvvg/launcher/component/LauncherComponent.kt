package com.hhvvg.launcher.component

abstract class LauncherComponent {
    abstract val className: String
    lateinit var instance: Any
}
