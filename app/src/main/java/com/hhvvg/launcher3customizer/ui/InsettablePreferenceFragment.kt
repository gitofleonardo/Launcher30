package com.hhvvg.launcher3customizer.ui

import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.preference.PreferenceFragmentCompat
import com.hhvvg.launcher3customizer.R

abstract class InsettablePreferenceFragment : PreferenceFragmentCompat() {
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
}
