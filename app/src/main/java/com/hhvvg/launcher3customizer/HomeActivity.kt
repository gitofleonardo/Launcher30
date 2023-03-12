package com.hhvvg.launcher3customizer

import android.graphics.Insets
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.color.DynamicColors
import com.hhvvg.launcher3customizer.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        DynamicColors.applyToActivityIfAvailable(this)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val label = destination.label
            label?.let { binding.collapsingToolbar.title = it }
        }
        navView.setupWithNavController(navController)

        ViewCompat.setOnApplyWindowInsetsListener(binding.navView) { view, windowInsets ->
            val navInsets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.apply {
                setPadding(paddingLeft, paddingTop, paddingRight, navInsets.bottom)
            }
            val actionBarInsets = windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
            binding.appBarLayout.apply {
                setPadding(paddingLeft, actionBarInsets.top, paddingRight, paddingBottom)
            }
            WindowInsetsCompat.CONSUMED
        }
    }
}