package com.hhvvg.launcher3customizer.ui.perapps

import android.content.ComponentName
import android.os.Bundle
import android.os.UserHandle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hhvvg.launcher.service.LauncherService
import com.hhvvg.launcher3customizer.R
import com.hhvvg.launcher3customizer.databinding.FragmentPerAppBinding

private const val ARG_PARAM_COMPONENT = "component"
private const val ARG_PARAM_USER = "user"

class PerAppFragment : Fragment(), MenuProvider {
    private var _binding: FragmentPerAppBinding? = null
    private val binding: FragmentPerAppBinding
        get() = _binding!!

    private val viewModel by viewModels<AppViewModel>()
    private val launcherService by lazy { LauncherService.getLauncherService() }

    private lateinit var component: ComponentName
    private lateinit var user: UserHandle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            component = it.getParcelable(ARG_PARAM_COMPONENT)!!
            user = it.getParcelable(ARG_PARAM_USER)!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPerAppBinding.inflate(inflater, container, false)

        activity?.addMenuProvider(this, viewLifecycleOwner)

        binding.applyButton.setOnClickListener {
            val label = binding.appLabelEt.text ?: ""
            launcherService.setComponentLabel(component, user, label)
        }

        viewModel.appItem.observe(viewLifecycleOwner) {
            binding.appLabelEt.setText(it.label)
            binding.iconDrawable.setImageDrawable(it.icon)
        }

        viewModel.loadApp(requireContext(), component, user)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_per_app, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.reset_menu -> {
                launcherService.resetAppFavorites(component, user)
                true
            }
            else -> false
        }
    }
}
