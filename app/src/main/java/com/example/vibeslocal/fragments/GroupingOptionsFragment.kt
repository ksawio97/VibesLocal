package com.example.vibeslocal.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.vibeslocal.R
import com.example.vibeslocal.databinding.FragmentGroupingOptionsBinding
import com.example.vibeslocal.viewmodels.GroupingOptionsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GroupingOptionsFragment : Fragment(R.layout.fragment_grouping_options) {
    private val viewModel: GroupingOptionsViewModel by viewModel()
    private lateinit var binding: FragmentGroupingOptionsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentGroupingOptionsBinding.bind(view)

        viewModel.configureRecyclerView(binding.groupingOptions, view.findNavController(), R.id.action_groupingOptionsFragment_to_musicItemsFragment)
    }
}