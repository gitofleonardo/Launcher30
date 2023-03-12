package com.hhvvg.launcher3customizer.ui.customize

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.hhvvg.launcher.service.LauncherService
import com.hhvvg.launcher3customizer.R
import com.hhvvg.launcher3customizer.view.ColorDialogPreference
import com.hhvvg.launcher3customizer.view.ColorPickerDialog

class CustomizeFragment : PreferenceFragmentCompat(), ColorPickerDialog.ColorCallback {
    private val launcherService by lazy { LauncherService.getLauncherService() }
    private val dataStore by lazy { CustomizationDataStore(launcherService, requireContext()) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = dataStore
        setPreferencesFromResource(R.xml.customization_preference_screen, rootKey)
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {
        if (preference is ColorDialogPreference) {
            preference.showDialog(preference, this)
        } else {
            super.onDisplayPreferenceDialog(preference)
        }
    }

    override fun onColorSelected(color: Int) {
        dataStore.putInt(getString(R.string.key_icon_dot_color), color)
    }

    override fun onColorRestore() {
        launcherService.restoreDotParamsColor()
    }
}
