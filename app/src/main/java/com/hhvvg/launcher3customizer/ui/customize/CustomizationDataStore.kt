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
            else -> defValue
        }
    }

    override fun putBoolean(key: String?, value: Boolean) {
        when (key) {
            stringOf(R.string.key_customized_icon_click_effect) -> {
                service.isClickEffectEnable = value
            }
        }
    }

    override fun getInt(key: String?, defValue: Int): Int {
        return when (key) {
            stringOf(R.string.key_icon_dot_color) -> {
                service.dotParamsColor
            }
            else -> super.getInt(key, defValue)
        }
    }

    override fun putInt(key: String?, value: Int) {
        when (key) {
            stringOf(R.string.key_icon_dot_color) -> {
                service.dotParamsColor = value
            }
        }
    }

    private fun stringOf(res: Int): String {
        return context.getString(res)
    }
}
