package com.hhvvg.launcher.repository

import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import android.os.UserHandle
import com.hhvvg.launcher.service.getSystemSharedPreferences
import com.hhvvg.launcher.user.ComponentKey

private const val SP_NAME = "AppLabelData"

/**
 * @author hhvvg
 */
class AppLabelRepository(private val context: Context) {

    private val dataPreference: SharedPreferences by lazy {
        context.getSystemSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    fun setLabelForComponent(component: ComponentName, user: UserHandle, label: CharSequence?) {
        val key = ComponentKey(component, user).toString()
        if (label == null) {
            dataPreference.edit().remove(key).apply()
            return
        }
        dataPreference.edit().putString(key, label.toString()).apply()
    }

    fun getLabelForComponent(component: ComponentName, user: UserHandle): String? {
        val key = ComponentKey(component, user).toString()
        return dataPreference.getString(key, null)
    }

    fun clearLabelForComponent(cn: ComponentName, user: UserHandle) {
        val key = ComponentKey(cn, user).toString()
        dataPreference.edit().remove(key).commit()
    }
}
