package com.hhvvg.launcher.utils

import android.app.Application
import com.hhvvg.launcher.Launcher
import com.hhvvg.launcher.LauncherApplication
import de.robv.android.xposed.XposedHelpers

var Application.launcher: Launcher
    get() = getAdditionalInstanceField(LauncherApplication.LAUNCHER)!!
    set(value) = setAdditionalInstanceField(LauncherApplication.LAUNCHER, value)

fun Any.setAdditionalInstanceField(name: String, obj: Any?) {
    XposedHelpers.setAdditionalInstanceField(this, name, obj)
}

fun <T> Any.getAdditionalInstanceField(name: String): T? {
    return XposedHelpers.getAdditionalInstanceField(this, name) as T?
}

fun <T> Any.getObjectField(name: String): T? {
    return XposedHelpers.getObjectField(this, name) as T?
}

fun Any.setObjectField(name: String, value: Any?) {
    XposedHelpers.setObjectField(this, name, value)
}

fun Any.getBooleanField(name: String): Boolean {
    return XposedHelpers.getBooleanField(this, name)
}

fun Any.setBooleanField(name: String, value: Boolean) {
    return XposedHelpers.setBooleanField(this, name, value)
}

fun <T> Class<*>.getStaticField(name: String): T? {
    return XposedHelpers.getStaticObjectField(this, name) as T?
}

fun <T> Class<*>.getAdditionalStaticField(name: String): T? {
    return XposedHelpers.getAdditionalStaticField(this, name) as T?
}

fun Class<*>.setAdditionalStaticField(name: String, obj: Any) {
    XposedHelpers.setAdditionalStaticField(this, name, obj)
}