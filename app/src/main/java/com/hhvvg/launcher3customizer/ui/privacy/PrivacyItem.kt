package com.hhvvg.launcher3customizer.ui.privacy

import android.content.ComponentName
import android.graphics.drawable.Drawable

data class PrivacyItem(
    val icon: Drawable,
    val appName: String,
    val component: ComponentName,
    var selected: Boolean = false
)
