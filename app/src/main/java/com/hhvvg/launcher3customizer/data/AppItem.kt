package com.hhvvg.launcher3customizer.data

import android.content.ComponentName
import android.graphics.drawable.Drawable
import android.os.UserHandle

/**
 * @author hhvvg
 */
data class AppItem(
    var icon: Drawable,
    var label: String,
    var component: ComponentName,
    var user: UserHandle
)
