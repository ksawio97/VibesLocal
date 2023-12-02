package com.example.vibeslocal.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.vibeslocal.databinding.ActivityPlaybackDetailsBinding
import com.example.vibeslocal.fragments.CurrentSongActionsFragment
import com.example.vibeslocal.fragments.QueueInfoFragment
import com.example.vibeslocal.viewmodels.CurrentPageViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaybackDetailsActivity : AppCompatActivity() {
    private val currentPageViewModel : CurrentPageViewModel by viewModel()
    private lateinit var binding : ActivityPlaybackDetailsBinding

    private lateinit var currentPageChanges: (Int) -> Unit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        binding = ActivityPlaybackDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingUpCurrentPageFragmentsConnection()
    }

    override fun onDestroy() {
        super.onDestroy()
        //clearing subscription
        currentPageViewModel.unsubscribe(currentPageChanges)
    }

    private fun settingUpCurrentPageFragmentsConnection() {
        binding.optionsPager.adapter =
            object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
                override fun getItemCount(): Int {
                    return 2
                }

                override fun createFragment(position: Int): Fragment {
                    return when (position) {
                        0 -> CurrentSongActionsFragment()
                        1 -> QueueInfoFragment()
                        else -> {
                            throw StackOverflowError("There shouldn't be more than 2 options!")
                        }
                    }
                }
            }

        //#region setting up changing currentItem in optionsPager based on tab
        binding.optionsPager.currentItem = currentPageViewModel.getCurrentItem()
        currentPageChanges = {
            if (binding.optionsPager.currentItem != it)
                binding.optionsPager.currentItem = it
        }
        currentPageViewModel.subscribe(currentPageChanges)
        //#endregion

        //adding action to inform changing page in optionsPager
        binding.optionsPager.registerOnPageChangeCallback(currentPageViewModel.onPageChangeCallback)
    }
}