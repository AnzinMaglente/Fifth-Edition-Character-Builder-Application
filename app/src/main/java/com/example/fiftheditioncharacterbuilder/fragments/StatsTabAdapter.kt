package com.example.fiftheditioncharacterbuilder.fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class StatsTabAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
        private val fragments = listOf(StatTab1Fragment(), StatTab2Fragment(), StatTab3Fragment())

        // Return the total number of fragments
        override fun getItemCount(): Int = fragments.size

        // Return the fragment at the specified position
        override fun createFragment(position: Int): Fragment = fragments[position]
}
