package com.hhvvg.launcher3customizer.ui.apps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hhvvg.launcher3customizer.R
import com.hhvvg.launcher3customizer.data.AppItem
import com.hhvvg.launcher3customizer.databinding.FragmentAllAppsBinding

class AllAppsFragment : Fragment() {

    private var _binding: FragmentAllAppsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val allAppsViewModel by viewModels<AllAppsViewModel>()
    private val allAppsList = ArrayList<AppItem>()
    private val allAppsAdapter = AllAppsAdapter(allAppsList).apply {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllAppsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.appList.adapter = allAppsAdapter
        allAppsAdapter.setOnItemClickListener {
            val action = AllAppsFragmentDirections.actionNavigationAllAppsToNavigationPerApp(it.component, it.user)
            findNavController().navigate(action)
        }
        binding.swipeToRefresh.setOnRefreshListener {
            context?.let { allAppsViewModel.loadAllApps(it) }
        }

        allAppsViewModel.allApps.observe(viewLifecycleOwner) {
            binding.swipeToRefresh.isRefreshing = false
            val size = allAppsList.size
            allAppsList.clear()
            allAppsAdapter.notifyItemRangeRemoved(0, size)
            allAppsList.addAll(it)
            allAppsAdapter.notifyItemRangeChanged(0, allAppsList.size)
            allAppsAdapter.notifyItemRangeInserted(0, allAppsList.size)
        }

        if (allAppsList.isEmpty()) {
            context?.let { allAppsViewModel.loadAllApps(it) }
        }
        return root
    }

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}