package com.hhvvg.launcher3customizer.ui.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.hhvvg.launcher.service.LauncherService
import com.hhvvg.launcher3customizer.R
import com.hhvvg.launcher3customizer.databinding.FragmentMoreSettingsBinding

class MoreSettingsFragment : PreferenceFragmentCompat() {
    private val service by lazy { LauncherService.getLauncherService() }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.more_preference_screen, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            getString(R.string.key_reload_launcher) -> {
                service.forceReloadLauncher()
                Toast.makeText(requireContext(), R.string.title_done_toast, Toast.LENGTH_SHORT).show()
                true
            }
            getString(R.string.key_clear_icon_cache) -> {
                service.iconPackProvider = service.iconPackProvider
                Toast.makeText(requireContext(), R.string.title_done_toast, Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }
}