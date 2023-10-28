package com.example.vibeslocal.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.adapters.OptionsListAdapter
import com.example.vibeslocal.models.OptionModel
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.repositories.SongsRepository
import org.koin.core.component.KoinComponent

class OptionsViewModel(private val songsRepository: SongsRepository) : ViewModel(), KoinComponent {
    fun <T> configureRecyclerView(
        recyclerView: RecyclerView,
        selector: (SongModel) -> T
    ) {
        recyclerView.setHasFixedSize(true)
        //it will be needed to pass it to shown activity
        val options = songsRepository.getGroupedSongs(selector)
        //TODO add slow loading to it
        val optionsList = options.keys.map{
            OptionModel(it.toString())
        }

        val optionsListAdapter = OptionsListAdapter(optionsList)
        recyclerView.adapter = optionsListAdapter

        optionsListAdapter.setOnItemClickListener(object: OptionsListAdapter.OnItemClickListener {
            override fun onItemClick(optionModel: OptionModel) {
                //TODO add here redirection to activity with songs
                Log.i("Debug", "Clicked ${optionModel.title}")
            }
        })
    }
}