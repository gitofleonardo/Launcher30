package com.hhvvg.launcher.repository

import android.content.Context
import com.hhvvg.launcher.service.getSystemSharedPreferences

private const val SP_NAME = "CommonPreferences.xml"

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

    fun setQsbEnable(enable: Boolean) {
        preferences.edit().putBoolean(KEY_QSB_ENABLE, enable).commit()
    }

    fun isQsbEnable(): Boolean {
        return preferences.getBoolean(KEY_QSB_ENABLE, true)
    }

    fun setOpenedFolderCenter(center: Boolean) {
        preferences.edit().putBoolean(KEY_OPENED_FOLDER_CENTER, center).commit()
    }

    fun isOpenedFolderCenter(): Boolean {
        return preferences.getBoolean(KEY_OPENED_FOLDER_CENTER, true)
    }

    fun setUseBiometricPrivacyApps(useBiometric: Boolean) {
        preferences.edit().putBoolean(KEY_USE_BIOMETRIC_PRIVACY_APPS, useBiometric).commit()
    }

    fun useBiometricPrivacyApps(): Boolean {
        return preferences.getBoolean(KEY_USE_BIOMETRIC_PRIVACY_APPS, false)
    }

    fun isPrivacyAppsHiddenFromRecents(): Boolean {
        return preferences.getBoolean(KEY_HIDE_PRIVACY_APPS_FROM_RECENTS, true)
    }

    fun setPrivacyAppsHiddenFromRecents(hidden: Boolean) {
        preferences.edit().putBoolean(KEY_HIDE_PRIVACY_APPS_FROM_RECENTS, hidden).commit()
    }

    fun setUseCustomSpringLoadedEffect(use: Boolean) {
        preferences.edit().putBoolean(KEY_USE_CUSTOM_SPRING_LOADED_EFFECT, use).commit()
    }

    fun isUseCustomSpringLoadedEffect(): Boolean {
        return preferences.getBoolean(KEY_USE_CUSTOM_SPRING_LOADED_EFFECT, true)
    }

    fun getIconScale(): Float {
        return preferences.getFloat(KEY_ICON_SCALE, 1.0f)
    }

    fun setIconScale(scale: Float) {
        preferences.edit().putFloat(KEY_ICON_SCALE, scale).commit()
    }

    fun setIconTextScale(scale: Float) {
        preferences.edit().putFloat(KEY_ICON_TEXT_SCALE, scale).commit()
    }

    fun getIconTextScale(): Float {
        return preferences.getFloat(KEY_ICON_TEXT_SCALE, 1.0f)
    }

    fun setIconDrawablePaddingScale(scale: Float) {
        preferences.edit().putFloat(KEY_ICON_DRAWABLE_PADDING_SCALE, scale).commit()
    }

    fun getIconDrawablePaddingScale(): Float {
        return preferences.getFloat(KEY_ICON_DRAWABLE_PADDING_SCALE, 1.0f)
    }

    fun setAllAppsIconTextVisible(visible: Boolean) {
        preferences.edit().putBoolean(KEY_ALL_APPS_ICON_TEXT_VISIBLE, visible).commit()
    }

    fun isAllAppsIconTextVisible(): Boolean {
        return preferences.getBoolean(KEY_ALL_APPS_ICON_TEXT_VISIBLE, true)
    }

    companion object {
        private const val KEY_ICON_CLICK_EFFECT_ENABLED = "KEY_ICON_CLICK_EFFECT_ENABLED"
        private const val KEY_DOT_PARAMS_COLOR = "KEY_DOT_PARAMS_COLOR"
        private const val KEY_ICON_PACK_PROVIDER = "KEY_ICON_PACK_PROVIDER"
        private const val KEY_ADAPTIVE_ICON_ENABLE = "KEY_ADAPTIVE_ICON_ENABLE"
        private const val KEY_ICON_TEXT_VISIBLE = "KEY_ICON_TEXT_VISIBLE"
        private const val KEY_NOTIFICATION_COUNT_ENABLE = "KEY_NOTIFICATION_COUNT_ENABLE"
        private const val KEY_SPRING_LOADED_BG_ENABLE = "KEY_SPRING_LOADED_BG_ENABLE"
        private const val KEY_QSB_ENABLE = "KEY_QSB_ENABLE"
        private const val KEY_OPENED_FOLDER_CENTER = "KEY_OPENED_FOLDER_CENTER"
        private const val KEY_USE_BIOMETRIC_PRIVACY_APPS = "KEY_USE_BIOMETRIC_PRIVACY_APPS"
        private const val KEY_HIDE_PRIVACY_APPS_FROM_RECENTS = "KEY_HIDE_PRIVACY_APPS_FROM_RECENTS"
        private const val KEY_USE_CUSTOM_SPRING_LOADED_EFFECT = "KEY_USE_CUSTOM_SPRING_LOADED_EFFECT"
        private const val KEY_ICON_SCALE = "KEY_ICON_SCALE"
        private const val KEY_ICON_TEXT_SCALE = "KEY_ICON_TEXT_SCALE"
        private const val KEY_ICON_DRAWABLE_PADDING_SCALE = "KEY_ICON_DRAWABLE_PADDING_SCALE"
        private const val KEY_ALL_APPS_ICON_TEXT_VISIBLE = "KEY_ALL_APPS_ICON_TEXT_VISIBLE"
    }
}
