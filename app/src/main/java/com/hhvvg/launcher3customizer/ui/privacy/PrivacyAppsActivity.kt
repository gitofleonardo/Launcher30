package com.hhvvg.launcher3customizer.ui.privacy

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.hhvvg.launcher3customizer.R
import com.hhvvg.launcher3customizer.databinding.ActivityPrivacyAppsBinding

private const val PRIVACY_APPS_COLUMNS = 4

class PrivacyAppsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacyAppsBinding
    private val viewModel by viewModels<PrivacyViewModel>()
    private val adapter by lazy { AllPrivacyAppsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyAppsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter.setOnAppClickListener {
            startApp(it.component)
        }
        binding.privacyAppsRv.adapter = adapter
        binding.privacyAppsRv.layoutManager = GridLayoutManager(this, PRIVACY_APPS_COLUMNS)

        viewModel.privacyAppsItem.observe(this) {
            adapter.update(it)

            binding.privacyAppsRv.isVisible = it.isNotEmpty()
            binding.noAppsHint.isVisible = it.isEmpty()
        }
        viewModel.loadAllPrivacyApps(this)
    }

    private fun startApp(cn: ComponentName) {
        val intent = Intent()
        intent.component = cn
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.text_fail_start_privacy_app), Toast.LENGTH_SHORT).show()
        }
    }
}