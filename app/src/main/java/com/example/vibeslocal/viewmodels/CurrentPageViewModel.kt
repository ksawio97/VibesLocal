package com.example.vibeslocal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import com.example.vibeslocal.events.CustomEvent
import com.example.vibeslocal.events.ICustomEventClass
import com.google.android.material.tabs.TabLayout

class CurrentPageViewModel : ViewModel(), ICustomEventClass<Int> {
    private val currentPageEvent = object : CustomEvent<Int>() {
        var currentItem = 0

        init {
            subscribe {
                currentItem = it
            }
        }
    }

    override fun subscribe(action: (Int) -> Unit) {
        currentPageEvent.subscribe(action)
    }

    override fun unsubscribe(action: (Int) -> Unit) {
        currentPageEvent.unsubscribe(action)
    }

    fun getCurrentItem(): Int {
        return currentPageEvent.currentItem
    }

    val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            if (tab != null)
                currentPageEvent.notify(tab.position)
        }
        override fun onTabReselected(tab: TabLayout.Tab?) {
            //TODO implement
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            //TODO implement
        }
    }

    val onPageChangeCallback = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            currentPageEvent.notify(position)
        }
    }
}