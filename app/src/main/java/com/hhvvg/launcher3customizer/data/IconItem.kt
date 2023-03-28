package com.hhvvg.launcher3customizer.data

import android.content.ComponentName
import android.content.res.Resources

open class IconItem(
    open val iconPackPkg: String,
    open val resources: Resources,
    open val resourceName: String,
)

data class IconCategoryItem(
    override val iconPackPkg: String,
    override val resources: Resources,
    override val resourceName: String,
    val category: String?
): IconItem(iconPackPkg, resources, resourceName)

data class IconComponentItem(
    override val iconPackPkg: String,
    override val resources: Resources,
    override val resourceName: String,
    val component: ComponentName
): IconItem(iconPackPkg, resources, resourceName)
