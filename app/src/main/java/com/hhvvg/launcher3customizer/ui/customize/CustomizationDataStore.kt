package com.hhvvg.launcher3customizer.ui.customize

import android.content.Context
import androidx.preference.PreferenceDataStore
import com.hhvvg.launcher.ILauncherService
import com.hhvvg.launcher.service.LauncherService
import com.hhvvg.launcher3customizer.R

class CustomizationDataStore(private val service: ILauncherService, private val context: Context) : PreferenceDataStore(){

    override fun getBoolean(key: String?, defValue: Boolean): Boolean {
        return when (key) {
            stringOf(R.string.key_customized_icon_click_effect) -> {
                service.isClickEffectEnable
            }
            stringOf(R.string.key_adaptive_icon_enable) -> {
                service.isAdaptiveIconEnable
            }
            else -> defValue
        }
    }

    override fun putBoolean(key: String?, value: Boolean) {
        when (key) {
            stringOf(R.string.key_customized_icon_click_effect) -> {
                service.isClickEffectEnable = value
            }
            stringOf(R.string.key_adaptive_icon_enable) -> {
                service.isAdaptiveIconEnable = value
            }
        }
    }

    private fun stringOf(res: Int): String {
        return context.getString(res)
    }
}
