package com.hhvvg.launcher.repository

import android.content.Context
import com.hhvvg.launcher.service.getSystemSharedPreferences

private const val SP_NAME = "CommonPreferences"

/**
 * @author hhvvg
 */
class CommonRepository(private val context: Context) {
    private val preferences by lazy {
        context.getSystemSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    fun setIconClickEffectEnable(enabled: Boolean) {
        preferences.edit().putBoolean(KEY_ICON_CLICK_EFFECT_ENABLED, enabled).commit()
    }

    fun isIconClickEffectEnable(): Boolean {
        return preferences.getBoolean(KEY_ICON_CLICK_EFFECT_ENABLED, true)
    }

    companion object {
        private const val KEY_ICON_CLICK_EFFECT_ENABLED = "KEY_ICON_CLICK_EFFECT_ENABLED"
    }
}