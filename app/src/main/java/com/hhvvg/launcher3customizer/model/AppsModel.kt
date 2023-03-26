package com.hhvvg.launcher3customizer.model

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.LauncherApps
import android.os.UserHandle
import android.util.DisplayMetrics
import com.hhvvg.launcher3customizer.data.AppItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author hhvvg
 */
class AppsModel {
    suspend fun loadApps(context: Context): List<AppItem> = withContext(context = Dispatchers.IO) {
        val la = context.getSystemService(LauncherApps::class.java)
        val activityInfos = la.getActivityList(null, UserHandle.getUserHandleForUid(0))
        activityInfos.map {
            AppItem(it.getIcon(DisplayMetrics.DENSITY_XXXHIGH), it.label.toString(), it.componentName, it.user)
        }.sortedBy {
            it.label
        }
    }

    suspend fun loadApp(context: Context, component: ComponentName, user: UserHandle): AppItem = withContext(context = Dispatchers.IO) {
        val la = context.getSystemService(LauncherApps::class.java)
        val intent = Intent().apply {
            setComponent(component)
        }
        val lai = la.resolveActivity(intent, user)
        val drawable = lai.getIcon(DisplayMetrics.DENSITY_XXXHIGH)
        AppItem(drawable, lai.label.toString(), component, user)
    }
}
