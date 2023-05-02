package com.hhvvg.launcher.repository

import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import com.hhvvg.launcher.service.getSystemSharedPreferences

private const val SP_NAME = "PrivacyApps.xml"

class PrivacyAppsRepository(private val context: Context) {
    private val dataPreference: SharedPreferences by lazy {
        context.getSystemSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    fun putPrivacyApp(cn: ComponentName, isPrivacy: Boolean) {
        val cnString = cn.flattenToString()
        val edit = dataPreference.edit()
        if (isPrivacy) {
            edit.putBoolean(cnString, true)
        } else {
            edit.remove(cnString)
        }
        edit.commit()
    }

    fun getPrivacyItems(): List<ComponentName> {
        return dataPreference
            .all
            .filter { it.value is Boolean }
            .map { it as Map.Entry<String, Boolean> }
            .filter { it.value }
            .mapNotNull { ComponentName.unflattenFromString(it.key) }
            .toList()
    }
}