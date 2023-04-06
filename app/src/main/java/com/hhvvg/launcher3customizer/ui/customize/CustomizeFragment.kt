package com.hhvvg.launcher3customizer.ui.customize

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.hhvvg.launcher.service.LauncherService
import com.hhvvg.launcher3customizer.R
import com.hhvvg.launcher3customizer.ui.customize.CustomizeFragmentDirections.actionNavigationCustomizationToNavigationIconPacks
import com.hhvvg.launcher3customizer.ui.customize.CustomizeFragmentDirections.actionNavigationCustomizationToNavigationNotificationColor

class CustomizeFragment : PreferenceFragmentCompat() {
    private val launcherService by lazy { LauncherService.getLauncherService() }
    private val dataStore by lazy { CustomizationDataStore(launcherService, requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, windowInsets ->
            val navInsets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
            val bottomInset = navInsets.bottom + view.context.resources.getDimensionPixelSize(R.dimen.navigation_height)
            v.apply {
                setPadding(paddingLeft, paddingTop, paddingRight, bottomInset)
            }
            WindowInsetsCompat.CONSUMED
        }
    }

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
            else -> false
        }
    }
}
