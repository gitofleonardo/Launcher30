package com.hhvvg.launcher3customizer.ui.privacy

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hhvvg.launcher.service.LauncherService
import com.hhvvg.launcher3customizer.model.AppsModel
import kotlinx.coroutines.launch

class PrivacyViewModel : ViewModel() {
    private val launcherService by lazy { LauncherService.getLauncherService() }
    private val appsModel = AppsModel()

    val appItems: MutableLiveData<List<PrivacyItem>> = MutableLiveData()
    val privacyAppsItem: MutableLiveData<List<PrivacyItem>> = MutableLiveData()

    fun loadAppItems(context: Context) {
        viewModelScope.launch {
            val privacyComponents = launcherService.privacyItems.toSet()

            val allApps = appsModel.loadApps(context)
            val privacyList = allApps
                .map {
                    PrivacyItem(it.icon, it.label, it.component)
                        .apply { this.selected = privacyComponents.contains(it.component) }
                }
                .sortedBy { !it.selected }
            appItems.value = privacyList
        }
    }

    fun loadAllPrivacyApps(context: Context) {
        viewModelScope.launch {
            val privacyComponents = launcherService.privacyItems.toSet()

            val allApps = appsModel.loadApps(context)
            val privacyList = allApps
                .map {
                    PrivacyItem(it.icon, it.label, it.component)
                        .apply { this.selected = privacyComponents.contains(it.component) }
                }
                .filter { it.selected }
            privacyAppsItem.value = privacyList
            Log.d("privacy", "load privacy apps: ${privacyList.size}")
        }
    }
}
