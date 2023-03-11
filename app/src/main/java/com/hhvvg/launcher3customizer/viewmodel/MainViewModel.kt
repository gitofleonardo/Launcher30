package com.hhvvg.launcher3customizer.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hhvvg.launcher3customizer.data.AppItem
import com.hhvvg.launcher3customizer.model.AppsModel
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val model = AppsModel()

    val allApps = MutableLiveData<List<AppItem>>(emptyList())

    fun loadAllApps(context: Context) {
        viewModelScope.launch {
            val apps = model.loadApps(context)
            allApps.value = apps
        }
    }
}
