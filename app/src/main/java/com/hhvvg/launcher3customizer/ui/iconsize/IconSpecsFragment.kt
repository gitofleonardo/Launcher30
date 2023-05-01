package com.hhvvg.launcher3customizer.ui.iconsize

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hhvvg.launcher.service.LauncherService
import com.hhvvg.launcher3customizer.databinding.FragmentIconSpecsBinding
import com.hhvvg.launcher3customizer.ui.InsettableFragment

class IconSpecsFragment : InsettableFragment() {
    private var _binding: FragmentIconSpecsBinding? = null
    private val binding: FragmentIconSpecsBinding
        get() = _binding!!

    private val service by lazy {
        LauncherService.getLauncherService()
    }

    private var newIconScale = 0.0f
    private var newIconTextScale = 0.0f
    private var newIconDrawablePaddingScale = 0.0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIconSpecsBinding.inflate(inflater, container, false)

        binding.iconSizeSlider.addOnChangeListener { _, value, _ ->
            newIconScale = value
        }

        binding.iconTextSizeSlider.addOnChangeListener { _, value, _ ->
            newIconTextScale = value
        }

        binding.iconDrawablePaddingScaleSlider.addOnChangeListener { _, value, _ ->
            newIconDrawablePaddingScale = value
        }

        binding.iconSizeSlider.setValues(boundToRange(service.iconScale, 0.1f, 2.0f))
        binding.iconTextSizeSlider.setValues(boundToRange(service.iconTextScale, 0.1f, 2.0f))
        binding.iconDrawablePaddingScaleSlider.setValues(boundToRange(service.iconDrawablePaddingScale, 0f, 2.0f))

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        applyScale()
    }

    private fun boundToRange(value: Float, min: Float, max: Float): Float {
        if (value < min) {
            return min
        } else if (value > max) {
            return max
        }
        return value
    }

    private fun applyScale() {
        if (service.iconScale != newIconScale) {
            service.iconScale = newIconScale
        }

        if (service.iconTextScale != newIconTextScale) {
            service.iconTextScale = newIconTextScale
        }

        if (service.iconDrawablePaddingScale != newIconDrawablePaddingScale) {
            service.iconDrawablePaddingScale = newIconDrawablePaddingScale
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}