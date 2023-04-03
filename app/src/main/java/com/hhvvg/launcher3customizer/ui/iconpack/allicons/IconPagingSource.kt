package com.hhvvg.launcher3customizer.ui.iconpack.allicons

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hhvvg.launcher3customizer.data.IconItem
import com.hhvvg.launcher3customizer.model.IconPacksModel
import kotlin.math.min

class IconPagingSource(private val model: IconPacksModel, private val context: Context, private val iconPackPkg: String) : PagingSource<Int, IconItem>() {
    private val allIconResourceItems = ArrayList<IconItem>()

    override fun getRefreshKey(state: PagingState<Int, IconItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, IconItem> {
        if (allIconResourceItems.isEmpty()) {
            allIconResourceItems.addAll(model.loadAllIconResources(context, iconPackPkg))
        }
        val nextPageNum = (params.key ?: 1) - 1
        val startIndex = nextPageNum * PER_PAGE_SIZE
        val endIndex = min((nextPageNum + 1) * PER_PAGE_SIZE, allIconResourceItems.size)
        val nextPage: Int?
        val result: List<IconItem>
        if (startIndex > endIndex) {
            result = emptyList()
            nextPage = null
        } else {
            result = allIconResourceItems.subList(startIndex, endIndex - 1)
            nextPage = nextPageNum + 2
        }
        return LoadResult.Page(
            data = result,
            prevKey = null,
            nextKey = nextPage
        )
    }

    companion object {
        const val PER_PAGE_SIZE = 20
    }
}
