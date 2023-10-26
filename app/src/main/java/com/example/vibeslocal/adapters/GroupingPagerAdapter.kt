package com.example.vibeslocal.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.vibeslocal.fragments.OptionsFragment

//TODO get items from GroupingModel
class GroupingPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 4
    }
    //TODO change grouping based on position
    override fun createFragment(position: Int): Fragment {
        return OptionsFragment()
    }
}