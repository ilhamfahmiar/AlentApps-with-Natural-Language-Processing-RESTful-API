package com.ifar.alentapps

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter


class TabsViewAdapter(fm: FragmentManager, lifecycle: Lifecycle, private var numberOfTabs: Int) : FragmentStateAdapter(fm, lifecycle) {

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> {
                // # Dasboard
                val bundle = Bundle()
                bundle.putString("fragmentName", "Dashboard")
                val dashboardFragment = DashboardMode()
                dashboardFragment.arguments = bundle
                return dashboardFragment
            }
//            1 -> {
//                // # IOT Fragment
//                val bundle = Bundle()
//                bundle.putString("fragmentName", "IoT Mode")
//                val iotFragment = IotMode()
//                iotFragment.arguments = bundle
//                return iotFragment
//            }
//            2 -> {
//                // # About Fragment
//                val bundle = Bundle()
//                bundle.putString("fragmentName", "About Mode")
//                val aboutFragment = AboutMode()
//                aboutFragment.arguments = bundle
//                return aboutFragment
//            }
            else -> return DashboardMode()
        }
    }

    override fun getItemCount(): Int {
        return numberOfTabs
    }
}