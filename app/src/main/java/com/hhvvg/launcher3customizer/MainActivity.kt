package com.hhvvg.launcher3customizer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.hhvvg.launcher3customizer.data.AppItem
import com.hhvvg.launcher3customizer.viewmodel.MainViewModel
import com.hhvvg.launcher3customizer.databinding.ActivityMainBinding
import com.hhvvg.launcher3customizer.databinding.LabelEditLayoutBinding
import com.hhvvg.launcher.service.LauncherService

class MainActivity : AppCompatActivity() {
    private val viewModel by lazy {
        MainViewModel()
    }
    private val launcherService by lazy {
        LauncherService.getLauncherService()
    }
    private lateinit var binding: ActivityMainBinding
    private val allAppsList = ArrayList<AppItem>()
    private val allAppsAdapter = AllAppsAdapter(allAppsList).apply {
        setOnItemClickListener(this@MainActivity::showLabelEditDialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appList.adapter = allAppsAdapter
        binding.appList.layoutManager = LinearLayoutManager(this)

        viewModel.allApps.observe(this) {
            allAppsList.clear()
            allAppsList.addAll(it)
            allAppsAdapter.notifyDataSetChanged()
        }

        viewModel.loadAllApps(applicationContext)
    }

    private fun showLabelEditDialog(item: AppItem) {
        val inputBinding = LabelEditLayoutBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(inputBinding.root)
            .setPositiveButton("OK") { _, _ ->
                var label: String? = inputBinding.appLabel.text.toString()
                if (label!!.isEmpty()) {
                    label = null
                }
                launcherService.setComponentLabel(item.component, item.user, label)
            }
        dialog.show()
    }
}
