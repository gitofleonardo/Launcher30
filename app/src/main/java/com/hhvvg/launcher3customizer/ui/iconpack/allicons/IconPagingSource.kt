package com.hhvvg.launcher3customizer.ui.iconpack.allicons

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hhvvg.launcher3customizer.data.IconDrawableItem
import com.hhvvg.launcher3customizer.data.IconItem
import com.hhvvg.launcher3customizer.model.IconPacksModel
import kotlin.math.min

class IconPagingSource(private val model: IconPacksModel, private val context: Context, private val iconPackPkg: String) : PagingSource<Int, IconDrawableItem>() {
    private val allIconResourceItems = ArrayList<IconItem>()

    override fun getRefreshKey(state: PagingState<Int, IconDrawableItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, IconDrawableItem> {
        if (allIconResourceItems.isEmpty()) {
            allIconResourceItems.addAll(model.loadAllIconResources(context, iconPackPkg))
        }
        val nextPageNum = (params.key ?: 1) - 1
        val startIndex = nextPageNum * PER_PAGE_SIZE
        val endIndex = min((nextPageNum + 1) * PER_PAGE_SIZE, allIconResourceItems.size)
        val result = ArrayList<IconDrawableItem>()
        for (i in startIndex until endIndex) {
            val resourceItem = allIconResourceItems[i]
            model.loadIconDrawable(resourceItem)?.let { result.add(it) }
        }
        return LoadResult.Page(
            data = result,
            prevKey = null,
            nextKey = nextPageNum + 2
        )
    }

    companion object {
        const val PER_PAGE_SIZE = 20
    }
}
