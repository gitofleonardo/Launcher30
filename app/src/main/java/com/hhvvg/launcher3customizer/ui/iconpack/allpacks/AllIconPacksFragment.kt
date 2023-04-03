package com.hhvvg.launcher3customizer.ui.iconpack.allpacks

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.hhvvg.launcher.service.LauncherService
import com.hhvvg.launcher3customizer.R
import com.hhvvg.launcher3customizer.data.IconPackItem
import com.hhvvg.launcher3customizer.databinding.FragmentAllIconPacksBinding

class AllIconPacksFragment : Fragment(), MenuProvider {
    private var _binding: FragmentAllIconPacksBinding? = null
    private val binding: FragmentAllIconPacksBinding
        get() = _binding!!

    private val launcherService by lazy {
        LauncherService.getLauncherService()
    }

    private val viewModel: AllIconPacksViewModel by viewModels()
    private val allIconPacks = ArrayList<IconPackItem>()
    private val adapter = AllIconPacksAdapter(allIconPacks)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAllIconPacksBinding.inflate(inflater, container, false)
        binding.iconPackItemsRv.adapter = adapter

        adapter.setOnItemClickListener {
            val action = AllIconPacksFragmentDirections.actionNavigationIconPacksToNavigationIconPackIcons(it.packageName)
            findNavController().navigate(action)
        }

        viewModel.allIconPackItems.observe(viewLifecycleOwner) {
            val size = allIconPacks.size
            allIconPacks.clear()
            adapter.notifyItemRangeRemoved(0, size)
            allIconPacks.addAll(it)
            adapter.notifyItemRangeChanged(0, allIconPacks.size)
            adapter.notifyItemRangeInserted(0, allIconPacks.size)
        }
        viewModel.loadAllIconPacks(requireContext())

        activity?.addMenuProvider(this, viewLifecycleOwner)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_icon_pack, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.reset_menu -> {
                launcherService.iconPackProvider = null
                Toast.makeText(requireContext(), R.string.title_done_toast, Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }
}
