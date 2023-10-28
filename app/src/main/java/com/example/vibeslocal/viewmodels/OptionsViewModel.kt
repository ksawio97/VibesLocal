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
    private lateinit var options : Map<*, List<SongModel>>
    private val optionsListAdapter = OptionsListAdapter()
    fun <T> loadGroupedSongs(selector: (SongModel) -> T) {
        options = songsRepository.getGroupedSongs(selector)
    }
    fun configureRecyclerView(
        recyclerView: RecyclerView
    ) {
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = optionsListAdapter

        optionsListAdapter.setOnItemClickListener(object: OptionsListAdapter.OnItemClickListener {
            override fun onItemClick(optionModel: OptionModel) {
                //TODO add here redirection to activity with songs
                Log.i("Debug", "Clicked ${optionModel.title}")
            }
        })
    }

    fun addOptions() {
        val optionsList = options.keys.map{
            OptionModel(it.toString())
        }

        optionsListAdapter.addItems(optionsList)
    }
}