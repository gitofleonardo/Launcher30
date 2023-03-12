package com.hhvvg.launcher3customizer.view

import android.content.Context
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.hhvvg.launcher3customizer.databinding.LayoutColorPickerBinding
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.flag.FlagMode

class ColorPickerDialog(context: Context) : BottomSheetDialog(context, R.style.Theme_Material3_DayNight_BottomSheetDialog) {
    private lateinit var binding: LayoutColorPickerBinding
    private var titleText: CharSequence? = null
    private var colorCallback: ColorCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.let { WindowCompat.setDecorFitsSystemWindows(it, false) }
        binding = LayoutColorPickerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setCancelable(false)

        binding.colorPicker.flagView = BubbleFlag(context).apply {
            flagMode = FlagMode.ALWAYS
        }

        if (titleText == null) {
            binding.titleView.isVisible = false
        } else {
            binding.titleView.text = titleText
        }

        binding.cancelButton.setOnClickListener {
            dismiss()
        }
        binding.confirmButton.setOnClickListener {
            colorCallback?.onColorSelected(binding.colorPicker.pureColor)
            dismiss()
        }
        binding.restoreButton.setOnClickListener {
            colorCallback?.onColorRestore()
            dismiss()
        }
    }

    override fun setTitle(title: CharSequence?) {
        titleText = title
    }

    @FunctionalInterface
    interface ColorCallback {
        fun onColorSelected(color: Int)
        fun onColorRestore()
    }

    fun setColorCallback(callback: ColorCallback) {
        colorCallback = callback
    }
}