package com.hhvvg.launcher.component

import android.view.View
import de.robv.android.xposed.XposedHelpers

abstract class ViewGroupComponent : ViewComponent() {
    fun getChildAt(index: Int): View {
        return XposedHelpers.callMethod(instance, "getChildAt", index) as View
    }
}