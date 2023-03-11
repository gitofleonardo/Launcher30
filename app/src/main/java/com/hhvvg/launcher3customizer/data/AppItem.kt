package com.hhvvg.launcher3customizer.data

import android.content.ComponentName
import android.graphics.drawable.Drawable
import android.os.UserHandle

/**
 * @author hhvvg
 */
data class AppItem(
    val icon: Drawable,
    val label: String,
    val component: ComponentName,
    val user: UserHandle
)
