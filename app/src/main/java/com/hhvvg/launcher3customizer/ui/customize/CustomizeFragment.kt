package com.hhvvg.launcher3customizer.ui.customize

import android.os.Bundle
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import com.hhvvg.launcher.service.LauncherService
import com.hhvvg.launcher3customizer.R
import com.hhvvg.launcher3customizer.ui.InsettablePreferenceFragment
import com.hhvvg.launcher3customizer.ui.customize.CustomizeFragmentDirections.actionNavigationCustomizationToIconSpecsFragment
import com.hhvvg.launcher3customizer.ui.customize.CustomizeFragmentDirections.actionNavigationCustomizationToNavigationIconPacks
import com.hhvvg.launcher3customizer.ui.customize.CustomizeFragmentDirections.actionNavigationCustomizationToNavigationNotificationColor

class CustomizeFragment : InsettablePreferenceFragment() {
    private val launcherService by lazy { LauncherService.getLauncherService() }
    private val dataStore by lazy { CustomizationDataStore(launcherService, requireContext()) }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = dataStore
        setPreferencesFromResource(R.xml.customization_preference_screen, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            getString(R.string.key_icon_dot_color) -> {
                val action = actionNavigationCustomizationToNavigationNotificationColor()
                findNavController().navigate(action)
                true
            }
            getString(R.string.key_icon_packs) -> {
                val action = actionNavigationCustomizationToNavigationIconPacks()
                findNavController().navigate(action)
                true
            }
            getString(R.string.key_icon_specs) -> {
                val action = actionNavigationCustomizationToIconSpecsFragment()
                findNavController().navigate(action)
                true
            }
            else -> false
        }
    }
}
