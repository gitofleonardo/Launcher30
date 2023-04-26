package com.hhvvg.launcher3customizer.ui.privacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.hhvvg.launcher.service.LauncherService
import com.hhvvg.launcher3customizer.databinding.FragmentPrivacySettingsBinding
import com.hhvvg.launcher3customizer.ui.InsettableFragment

class PrivacyAppSettingsFragment : InsettableFragment() {
    private var _binding: FragmentPrivacySettingsBinding? = null
    private val binding
        get() = _binding!!

    private val launcherService by lazy { LauncherService.getLauncherService() }
    private val adapter = PrivacyAppsAdapter().apply {
        setOnSelectionChangedListener { privacyItem, isPrivacy ->
            launcherService.changePrivacyItem(privacyItem.component, isPrivacy)
        }
    }
    private val viewModel by viewModels<PrivacyViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrivacySettingsBinding.inflate(inflater, container, false)
        binding.appsRv.adapter = adapter
        binding.appsRv.layoutManager = LinearLayoutManager(requireContext())
        binding.swipeRefresh.setOnRefreshListener { viewModel.loadAppItems(requireContext()) }

        viewModel.appItems.observe(viewLifecycleOwner) {
            adapter.update(it)
            binding.swipeRefresh.isRefreshing = false
        }
        viewModel.loadAppItems(requireContext())
        binding.swipeRefresh.isRefreshing = true
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}