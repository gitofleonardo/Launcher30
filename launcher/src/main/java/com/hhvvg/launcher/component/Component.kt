package com.hhvvg.launcher.component

abstract class Component {
    var instance: Any? = null
    val instanceNonNull: Any
        get() = instance!!

    fun <T> getTargetInstanceNonNull(): T {
        return instanceNonNull as T
    }

    fun <T> getTargetInstance(): T? {
        return instance as T?
    }
}
