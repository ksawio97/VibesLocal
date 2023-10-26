package com.example.vibeslocal.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.adapters.OptionsListAdapter
import com.example.vibeslocal.models.OptionModel

class OptionsViewModel : ViewModel() {
    fun configureRecyclerView(
        recyclerView: RecyclerView
    ) {
        recyclerView.setHasFixedSize(true)
        val options = listOf(
            OptionModel("Playlists"), OptionModel("Albums"),
            OptionModel("Artists"), OptionModel("Genres"))
        val optionsListAdapter = OptionsListAdapter(options)
        recyclerView.adapter = optionsListAdapter

        optionsListAdapter.setOnItemClickListener(object: OptionsListAdapter.OnItemClickListener {
            override fun onItemClick(songModel: OptionModel) {
                //TODO add here redirection to activity with songs
                Log.i("Debug", "Clicked ${songModel.title}")
            }
        })
    }
}