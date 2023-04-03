package com.hhvvg.launcher3customizer.glide

import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.data.DataFetcher
import com.hhvvg.launcher3customizer.data.IconItem

class IconFetcher(private val icon: IconItem) : DataFetcher<Drawable> {
    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in Drawable>) {
        val id = icon.resources.getIdentifier(icon.resourceName,
            "drawable", icon.iconPackPkg)
        if (id < 0) {
            callback.onLoadFailed(Exception("Resource not found"))
        }
        val drawable = try {
            ResourcesCompat.getDrawable(icon.resources, id, null)
        } catch (e: Exception) {
            null
        }
        if (drawable == null) {
            callback.onLoadFailed(Exception("Resource not found"))
        } else {
            callback.onDataReady(drawable)
        }
    }

    override fun cleanup() { }

    override fun cancel() { }

    override fun getDataClass(): Class<Drawable> {
        return Drawable::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.LOCAL
    }
}