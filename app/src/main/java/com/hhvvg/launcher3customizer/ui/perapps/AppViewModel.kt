package com.hhvvg.launcher3customizer.ui.perapps

import android.content.ComponentName
import android.content.Context
import android.os.UserHandle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hhvvg.launcher.service.LauncherService
import com.hhvvg.launcher3customizer.data.AppItem
import com.hhvvg.launcher3customizer.model.AppsModel
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {
    private val model = AppsModel()
    private val launcherService by lazy { LauncherService.getLauncherService() }

    val appItem = MutableLiveData<AppItem>()

    fun loadApp(context: Context, componentName: ComponentName, userHandle: UserHandle) {
        viewModelScope.launch {
            val app = model.loadApp(context, componentName, userHandle)
            launcherService.getComponentLabel(componentName, userHandle)?.let {
                app.label = it.toString()
            }
            appItem.value = app
        }
    }
}
