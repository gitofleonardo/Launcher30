package com.hhvvg.launcher3customizer.view

import android.content.Context
import android.util.AttributeSet

import androidx.preference.DialogPreference

class ColorDialogPreference(context: Context, attrs: AttributeSet?, defAttrStyle: Int) : DialogPreference(context, attrs, defAttrStyle, com.hhvvg.launcher3customizer.R.style.Preference_PreferenceCompat_Material3) {
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context): this(context, null)

    private var dialog: ColorPickerDialog? = null

    fun showDialog(pref: ColorDialogPreference, callback: ColorPickerDialog.ColorCallback) {
        dialog?.dismiss()
        dialog = ColorPickerDialog(pref.context).apply {
            setTitle(pref.title)
            setColorCallback(callback)
            show()
        }
    }
}
