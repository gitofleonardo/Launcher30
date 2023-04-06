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

    fun setAdaptiveIconEnable(enabled: Boolean) {
        preferences.edit().putBoolean(KEY_ADAPTIVE_ICON_ENABLE, enabled).commit()
    }

    fun isAdaptiveIconEnable(): Boolean {
        return preferences.getBoolean(KEY_ADAPTIVE_ICON_ENABLE, false)
    }

    fun setIconTextVisible(visible: Boolean) {
        preferences.edit().putBoolean(KEY_ICON_TEXT_VISIBLE, visible).commit()
    }

    fun isIconTextVisible(): Boolean {
        return preferences.getBoolean(KEY_ICON_TEXT_VISIBLE, true)
    }

    fun setDrawNotificationCount(draw: Boolean) {
        preferences.edit().putBoolean(KEY_NOTIFICATION_COUNT_ENABLE, draw).commit()
    }

    fun isDrawNotificationCount(): Boolean {
        return preferences.getBoolean(KEY_NOTIFICATION_COUNT_ENABLE, true)
    }

    fun setSpringLoadedBgEnable(enable: Boolean) {
        preferences.edit().putBoolean(KEY_SPRING_LOADED_BG_ENABLE, enable).commit()
    }

    fun isSpringLoadedBgEnable(): Boolean {
        return preferences.getBoolean(KEY_SPRING_LOADED_BG_ENABLE, false)
    }

    companion object {
        private const val KEY_ICON_CLICK_EFFECT_ENABLED = "KEY_ICON_CLICK_EFFECT_ENABLED"
        private const val KEY_DOT_PARAMS_COLOR = "KEY_DOT_PARAMS_COLOR"
        private const val KEY_ICON_PACK_PROVIDER = "KEY_ICON_PACK_PROVIDER"
        private const val KEY_ADAPTIVE_ICON_ENABLE = "KEY_ADAPTIVE_ICON_ENABLE"
        private const val KEY_ICON_TEXT_VISIBLE = "KEY_ICON_TEXT_VISIBLE"
        private const val KEY_NOTIFICATION_COUNT_ENABLE = "KEY_NOTIFICATION_COUNT_ENABLE"
        private const val KEY_SPRING_LOADED_BG_ENABLE = "KEY_SPRING_LOADED_BG_ENABLE"
    }
}
