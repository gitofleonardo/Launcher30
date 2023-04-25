package com.hhvvg.launcher3customizer.ui.privacy

import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.Preference.OnPreferenceChangeListener
import androidx.preference.PreferenceDataStore
import com.hhvvg.launcher.service.LauncherService
import com.hhvvg.launcher.utils.BiometricUtil
import com.hhvvg.launcher3customizer.R
import com.hhvvg.launcher3customizer.ui.InsettablePreferenceFragment
import com.hhvvg.launcher3customizer.view.MaterialSwitchPreference

private const val BIOMETRIC_TYPE = BiometricUtil.BIOMETRIC_NON_CREDENTIAL

class PrivacyAppsSettingsFragment : InsettablePreferenceFragment(), OnPreferenceChangeListener {

    private val service = LauncherService.getLauncherService()
    private val dataStore  = object : PreferenceDataStore() {
        override fun getBoolean(key: String?, defValue: Boolean): Boolean {
            return when (key) {
                getString(R.string.key_privacy_enable_biometric) -> {
                    service.useBiometricPrivacyApps()
                }
                else -> false
            }
        }

        override fun putBoolean(key: String?, value: Boolean) {
            when (key) {
                getString(R.string.key_privacy_enable_biometric) -> {
                    service.setBiometricPrivacyApps(value)
                }
            }
        }
    }
    private lateinit var biometricsSwitch: MaterialSwitchPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.preferenceDataStore = dataStore
        setPreferencesFromResource(R.xml.privacy_apps_prefs_screen, rootKey)

        biometricsSwitch = findPreference(getString(R.string.key_privacy_enable_biometric))!!
        biometricsSwitch.isEnabled = biometricAvailable()
        biometricsSwitch.onPreferenceChangeListener = this
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        return when (preference.key) {
            getString(R.string.key_all_privacy_apps) -> {
                navigatePrivacyApps()
                true
            }
            else -> false
        }
    }

    private fun switchOffBiometric() {
        biometricsSwitch.onPreferenceChangeListener = null
        biometricsSwitch.isChecked = false
        biometricsSwitch.onPreferenceChangeListener = this
    }

    private fun switchOnBiometric() {
        biometricsSwitch.onPreferenceChangeListener = null
        biometricsSwitch.isChecked = true
        biometricsSwitch.onPreferenceChangeListener = this
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any): Boolean {
        val enabled = newValue as Boolean
        if (enabled) {
            authenticate(
                this::switchOnBiometric,
                null
            )
        } else {
            authenticate(
                this::switchOffBiometric,
                null
            )
        }
        return false
    }

    private fun useBiometric(): Boolean {
        return preferenceManager.preferenceDataStore
            ?.getBoolean(getString(R.string.key_privacy_enable_biometric), false) ?: false
    }

    private fun biometricAvailable(): Boolean {
        return BiometricUtil.isBiometricAvailable(requireContext(), BIOMETRIC_TYPE)
    }

    private fun navigatePrivacyApps() {
        if (biometricAvailable() && useBiometric()) {
            authenticate(this::realNavigatePrivacyApps) {
                Toast.makeText(requireContext(), getString(R.string.title_auth_failed), Toast.LENGTH_SHORT).show()
            }
            return
        }
        realNavigatePrivacyApps()
    }

    private fun authenticate(successCallback: Runnable?, failureCallback: Runnable?) {
        BiometricUtil.showBiometricPrompt(
            this,
            BIOMETRIC_TYPE,
            getString(R.string.title_authenticate),
            getString(R.string.subtitle_auth),
            getString(R.string.cancel),
            successCallback,
            failureCallback
        )
    }

    private fun realNavigatePrivacyApps() {
        val action = PrivacyAppsSettingsFragmentDirections.actionPrivacyAppsSettingsFragmentToPrivacySettingsFragment()
        findNavController().navigate(action)
    }
}
