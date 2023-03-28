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

    fun setDotParamsColor(color: Int) {
        preferences.edit().putInt(KEY_DOT_PARAMS_COLOR, color).commit()
    }

    fun removeDotParamsColor() {
        preferences.edit().remove(KEY_DOT_PARAMS_COLOR).commit()
    }

    fun getDotParamsColor(): Int {
        return preferences.getInt(KEY_DOT_PARAMS_COLOR, -1)
    }

    fun setIconPackProvider(provider: String?) {
        preferences.edit().putString(KEY_ICON_PACK_PROVIDER, provider).commit()
    }

    fun getIconPackProvider(): String? {
        return preferences.getString(KEY_ICON_PACK_PROVIDER, null)
    }

    companion object {
        private const val KEY_ICON_CLICK_EFFECT_ENABLED = "KEY_ICON_CLICK_EFFECT_ENABLED"
        private const val KEY_DOT_PARAMS_COLOR = "KEY_DOT_PARAMS_COLOR"
        private const val KEY_ICON_PACK_PROVIDER = "KEY_ICON_PACK_PROVIDER"
    }
}
