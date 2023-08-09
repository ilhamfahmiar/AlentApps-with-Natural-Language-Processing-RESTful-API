package com.ifar.alentapps

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ifar.alentapps.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tabs Customization
        binding.tabsvt.setSelectedTabIndicatorColor(Color.WHITE)
        binding.tabsvt.setBackgroundColor(ContextCompat.getColor(this, R.color.aqua))
        // Set different Text Color for Tabs for when are selected or not
        //tab_layout.setTabTextColors(R.color.normalTabTextColor, R.color.selectedTabTextColor)

        // Number Of Tabs
        val numberOfTabs = 1

        // Set Tabs in the center
        //tab_layout.tabGravity = TabLayout.GRAVITY_CENTER
        // Show all Tabs in screen
        binding.tabsvt.tabMode = TabLayout.MODE_FIXED
        // Scroll to see all Tabs
        //tab_layout.tabMode = TabLayout.MODE_SCROLLABLE

        // Set Tab icons next to the text, instead above the text
        binding.tabsvt.isInlineLabel = true


        // Set the ViewPager Adapter
        val adapter = TabsViewAdapter(supportFragmentManager, lifecycle, numberOfTabs)
        binding.viewvt.adapter = adapter
        // Enable Swipe
        binding.viewvt.isUserInputEnabled = true

        // Link the TabLayout and the ViewPager2 together and Set Text & Icons
        TabLayoutMediator(binding.tabsvt, binding.viewvt) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "               Dashboard               "
//                    tab.setIcon(R.drawable.dashboard)
                }
//                1 -> {
//                    tab.text = "IOT MODE"
//                    //tab.setIcon(R.drawable.ic_launcher_background)
//
//                }
//                2 -> {
//                    tab.text = "ABOUT"
//                    //tab.setIcon(R.drawable.ic_launcher_background)
//                }

            }
            // Change color of the icons
            tab.icon?.colorFilter =
                BlendModeColorFilterCompat.createBlendModeColorFilterCompat(
                    Color.WHITE,
                    BlendModeCompat.SRC_ATOP
                )
        }.attach()

    }
}