package com.hhvvg.launcher3customizer.ui.iconpack.allicons

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.hhvvg.launcher.service.LauncherService
import com.hhvvg.launcher3customizer.R
import com.hhvvg.launcher3customizer.data.IconDrawableItem
import com.hhvvg.launcher3customizer.data.IconPackItem
import com.hhvvg.launcher3customizer.databinding.FragmentIconPackDetailsBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class IconPackDetailsFragment : Fragment(), MenuProvider {
    private var _binding: FragmentIconPackDetailsBinding? = null
    private val binding: FragmentIconPackDetailsBinding
        get() = _binding!!

    private val launcherService by lazy {
        LauncherService.getLauncherService()
    }

    private val viewModel: IconPackDetailsViewModel by viewModels()
    private val adapter = IconListAdapter()
    private lateinit var iconPack: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            iconPack = it.getString(ARG_ICON_PKG, null)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIconPackDetailsBinding.inflate(inflater, container, false)
        binding.iconPackIconsRv.adapter = adapter
        binding.iconPackIconsRv.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.refreshLayout.setOnRefreshListener {
            viewModel.pagingSource = IconPagingSource(viewModel.model, requireContext(), iconPack)
            adapter.refresh()
        }

        viewModel.pagingSource = IconPagingSource(viewModel.model, requireContext(), iconPack)
        lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                binding.refreshLayout.isRefreshing = it.refresh is LoadState.Loading
            }
        }
        lifecycleScope.launch {
            viewModel.iconFlow.collectLatest {
                adapter.submitData(it)
            }
        }

        activity?.addMenuProvider(this, viewLifecycleOwner)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_ICON_PKG = "icon_pkg"
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_icon_pack_icon, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.apply -> {
                launcherService.setIconPackProvider(iconPack)
                true
            }
            else -> false
        }
    }
}