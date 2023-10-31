package com.example.vibeslocal.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.vibeslocal.R
import com.example.vibeslocal.databinding.FragmentNavigationTabBinding
import com.example.vibeslocal.viewmodels.CurrentPageViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class NavigationTabFragment : Fragment(R.layout.fragment_navigation_tab) {
    private val currentPageViewModel: CurrentPageViewModel by activityViewModel()
    private lateinit var binding: FragmentNavigationTabBinding

    private lateinit var currentPageChanges: (Int) -> Unit
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNavigationTabBinding.bind(view)

        currentPageChanges = {
            if (binding.root.selectedTabPosition != it)
                binding.root.selectTab(binding.root.getTabAt(it))
        }
        currentPageViewModel.subscribe(currentPageChanges)
        binding.root.addOnTabSelectedListener(currentPageViewModel.onTabSelectedListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        currentPageViewModel.unsubscribe(currentPageChanges)
    }
}