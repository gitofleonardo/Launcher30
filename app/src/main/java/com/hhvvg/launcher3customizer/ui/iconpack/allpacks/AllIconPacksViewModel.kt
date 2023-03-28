package com.hhvvg.launcher3customizer.ui.iconpack.allpacks

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hhvvg.launcher3customizer.data.IconPackItem
import com.hhvvg.launcher3customizer.model.IconPacksModel
import kotlinx.coroutines.launch

class AllIconPacksViewModel : ViewModel() {
    private val iconPackModel = IconPacksModel()

    val allIconPackItems = MutableLiveData<List<IconPackItem>>()

    fun loadAllIconPacks(context: Context) {
        viewModelScope.launch {
            val iconPacks = iconPackModel.loadAllIconPacks(context)
            allIconPackItems.value = iconPacks
        }
    }
}
