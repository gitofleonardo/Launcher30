package com.hhvvg.launcher3customizer.ui.notificationcolor

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.view.MenuProvider
import com.hhvvg.launcher.service.LauncherService
import com.hhvvg.launcher3customizer.R
import com.hhvvg.launcher3customizer.databinding.FragmentNotificationColorPickerBinding
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.flag.FlagMode

class NotificationColorPickerFragment : Fragment(), MenuProvider {
    private val launcherService by lazy { LauncherService.getLauncherService() }

    private var _binding: FragmentNotificationColorPickerBinding? = null
    private val binding: FragmentNotificationColorPickerBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationColorPickerBinding.inflate(inflater, container, false)
        binding.colorPicker.flagView = BubbleFlag(context).apply {
            flagMode = FlagMode.ALWAYS
        }
        binding.colorPicker.setInitialColor(launcherService.dotParamsColor)

        activity?.addMenuProvider(this, viewLifecycleOwner)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.menu_notification_color, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.reset_menu -> {
                launcherService.restoreDotParamsColor()
                Toast.makeText(requireContext(), R.string.desc_reset_notification_color, Toast.LENGTH_SHORT).show()
                true
            }
            R.id.apply -> {
                launcherService.dotParamsColor = binding.colorPicker.pureColor
                Toast.makeText(requireContext(), R.string.title_notification_dot_color_applied, Toast.LENGTH_SHORT).show()
                true
            }
            else -> false
        }
    }
}
