package com.hhvvg.launcher.component

import android.content.Context
import android.view.View
import de.robv.android.xposed.XposedHelpers

abstract class ViewComponent : Component() {
    fun invalidate() {
        (instance as View).invalidate()
    }

    fun postInvalidate() {
        (instance as View).invalidate()
    }

    fun getContext(): Context {
        return XposedHelpers.callMethod(instance, "getContext") as Context
    }
}