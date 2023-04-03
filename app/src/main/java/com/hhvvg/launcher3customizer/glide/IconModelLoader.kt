package com.hhvvg.launcher3customizer.glide

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.model.ModelLoader
import com.hhvvg.launcher3customizer.data.IconItem

class IconModelLoader<T : IconItem> : ModelLoader<T, Drawable> {

    override fun buildLoadData(
        p0: T,
        p1: Int,
        p2: Int,
        p3: Options
    ): ModelLoader.LoadData<Drawable> {
        val fetcher = IconFetcher(p0)
        return ModelLoader.LoadData(p3, fetcher)
    }

    override fun handles(p0: T): Boolean {
        return true
    }
}