package com.hhvvg.launcher.component

import android.content.Context
import android.view.View

abstract class ViewComponent : Component() {

    val view: View
        get() = getTargetInstanceNonNull()

    fun invalidate() {
        view.invalidate()
    }

    fun postInvalidate() {
        view.postInvalidate()
    }

    fun getContext(): Context = view.context
}