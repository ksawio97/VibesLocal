package com.example.vibeslocal.fragments
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.vibeslocal.R
import com.example.vibeslocal.databinding.FragmentToolbarPlaybackDetailsBinding
import com.example.vibeslocal.viewmodels.CurrentPageViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class PlaybackDetailsToolbarFragment: Fragment(R.layout.fragment_toolbar_playback_details) {
    private val currentPageViewModel: CurrentPageViewModel by activityViewModel()
    private lateinit var binding: FragmentToolbarPlaybackDetailsBinding

    private lateinit var currentPageChanges: (Int) -> Unit
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentToolbarPlaybackDetailsBinding.bind(view)

        binding.goBackButton.setOnClickListener {
            Log.i(TAG, "goBackButton clicked")
            activity?.finish()
        }

        settingUpTabLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //clearing subscription
        currentPageViewModel.unsubscribe(currentPageChanges)
    }

    private fun settingUpTabLayout() {
        currentPageChanges = {
            if (binding.tabLayout.selectedTabPosition != it) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(it))
                Log.i(TAG, "selectedTabPosition changed")
            }
        }

        currentPageViewModel.subscribe(currentPageChanges)
        binding.tabLayout.addOnTabSelectedListener(currentPageViewModel.onTabSelectedListener)
    }

    companion object {
        const val TAG = "PlaybackDetailsToolbarFragment"
    }
}