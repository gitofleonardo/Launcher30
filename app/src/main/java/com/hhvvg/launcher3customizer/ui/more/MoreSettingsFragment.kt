package com.hhvvg.launcher3customizer.ui.more

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.Preference
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.hhvvg.launcher.service.LauncherService
import com.hhvvg.launcher3customizer.R
import com.hhvvg.launcher3customizer.ui.InsettablePreferenceFragment

private const val GITHUB_URL = "https://github.com/gitofleonardo/Launcher30"

class MoreSettingsFragment : InsettablePreferenceFragment() {
    private val service by lazy { LauncherService.getLauncherService() }

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
            getString(R.string.key_proj_source) -> {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(GITHUB_URL)
                startActivity(intent)
                true
            }
            getString(R.string.key_proj_oss) -> {
                val intent = Intent(requireContext(), OssLicensesMenuActivity::class.java)
                startActivity(intent)
                true
            }
            else -> false
        }
    }
}