package com.example.vibeslocal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.adapters.GroupingOptionsListAdapter
import com.example.vibeslocal.models.GroupingOptionModel

class GroupingOptionsViewModel : ViewModel() {
    fun configureRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        val options = listOf(
            GroupingOptionModel("Playlists"), GroupingOptionModel("Albums"),
            GroupingOptionModel("Artists"), GroupingOptionModel("Genres"))
        val groupingOptionsListAdapter = GroupingOptionsListAdapter(options)
        recyclerView.adapter = groupingOptionsListAdapter
    }
}