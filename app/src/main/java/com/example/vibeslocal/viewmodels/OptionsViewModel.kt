package com.example.vibeslocal.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.adapters.OptionsListAdapter
import com.example.vibeslocal.models.OptionModel
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.models.getParameterDisplayValue
import com.example.vibeslocal.models.getThumbnailFactory
import com.example.vibeslocal.repositories.SongsRepository
import org.koin.core.component.KoinComponent

class OptionsViewModel(private val songsRepository: SongsRepository) : ViewModel(), KoinComponent {
    private lateinit var options : Map<*, List<SongModel>>
    private lateinit var selector : (SongModel) -> Any?
    private val optionsListAdapter = OptionsListAdapter()

    fun <T> loadGroupedSongs(selector: (SongModel) -> T) {
        options = songsRepository.getGroupedSongs(selector)
        this.selector = selector
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
        val getThumbnail = getThumbnailFactory()
        val optionsList = options.map{
            val firstOption = it.value.first()
            OptionModel(firstOption.getParameterDisplayValue(selector), firstOption.getThumbnail(), it.value.size)
        }

        optionsListAdapter.addItems(optionsList)
    }
}