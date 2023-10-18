package com.example.vibeslocal.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.vibeslocal.R
import com.example.vibeslocal.viewmodels.GroupingOptionsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GroupingOptionsFragment : Fragment() {
    private val viewModel: GroupingOptionsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_grouping_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.configureRecyclerView(view.findViewById(R.id.grouping_options))
    }
}