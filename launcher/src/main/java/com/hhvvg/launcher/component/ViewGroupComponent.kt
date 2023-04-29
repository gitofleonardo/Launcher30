package com.hhvvg.launcher.component

import android.view.View
import android.view.ViewGroup
import de.robv.android.xposed.XposedHelpers

abstract class ViewGroupComponent : ViewComponent() {
    fun getChildAt(index: Int): View = viewGroup.getChildAt(index)

    val viewGroup: ViewGroup
        get() = getTargetInstanceNonNull()
}