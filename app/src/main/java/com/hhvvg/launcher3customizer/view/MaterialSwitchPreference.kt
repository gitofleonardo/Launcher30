package com.hhvvg.launcher3customizer.view

import android.content.Context
import android.util.AttributeSet
import androidx.preference.SwitchPreferenceCompat
import com.hhvvg.launcher3customizer.R

class MaterialSwitchPreference(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SwitchPreferenceCompat(context, attrs, defStyleAttr, R.style.Preference_SwitchPreferenceCompat_Material3) {
    constructor(context: Context): this(context, null)
    constructor(context: Context,attrs: AttributeSet?): this(context, attrs, 0)
}
