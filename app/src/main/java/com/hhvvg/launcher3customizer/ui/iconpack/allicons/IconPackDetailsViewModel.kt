package com.hhvvg.launcher3customizer.ui.iconpack.allicons

import androidx.lifecycle.ViewModel
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.hhvvg.launcher3customizer.model.IconPacksModel

class IconPackDetailsViewModel : ViewModel() {
    val model = IconPacksModel()
    var pagingSource: IconPagingSource? = null

    val iconFlow = Pager(
        PagingConfig(pageSize = IconPagingSource.PER_PAGE_SIZE),
    ) {
        pagingSource!!
    }.flow
}
