package com.hhvvg.launcher3customizer.glide

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.hhvvg.launcher3customizer.data.IconCategoryItem
import com.hhvvg.launcher3customizer.data.IconComponentItem
import com.hhvvg.launcher3customizer.data.IconItem

@GlideModule
class GlideModule : AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.prepend(IconItem::class.java, Drawable::class.java, IconLoadFactory())
        registry.prepend(IconCategoryItem::class.java, Drawable::class.java, IconLoadFactory())
        registry.prepend(IconComponentItem::class.java, Drawable::class.java, IconLoadFactory())
    }
}