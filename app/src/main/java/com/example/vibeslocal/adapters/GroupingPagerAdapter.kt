package com.example.vibeslocal.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.vibeslocal.fragments.OptionsFragment
import com.example.vibeslocal.models.IGroupingDescriptionStrategy
import com.example.vibeslocal.models.SongModel

class GroupingPagerAdapter<T>(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val groupingSelectors: List<((SongModel) -> T)>,
    private val groupingDescriptionStrategyGetter: (position: Int) -> IGroupingDescriptionStrategy): FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return groupingSelectors.size
    }

    override fun createFragment(position: Int): Fragment {
        return OptionsFragment(groupingSelectors[position], groupingDescriptionStrategyGetter(position))
    }
}