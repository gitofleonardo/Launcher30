package com.hhvvg.launcher3customizer.glide

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.hhvvg.launcher3customizer.data.IconItem

class IconLoadFactory<T: IconItem> : ModelLoaderFactory<T, Drawable> {
    override fun build(p0: MultiModelLoaderFactory): ModelLoader<T, Drawable> {
        return IconModelLoader()
    }

    override fun teardown() { }
}