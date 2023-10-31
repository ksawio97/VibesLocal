package com.example.vibeslocal.viewmodels

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
    private lateinit var options : List<List<SongModel>>
    private lateinit var selector : (SongModel) -> Any?
    private val optionsListAdapter = OptionsListAdapter()

    fun <T> loadGroupedSongs(selector: (SongModel) -> T) {
        options = songsRepository.getGroupedSongs(selector).values.toList()
        this.selector = selector
    }
    fun configureRecyclerView(
        recyclerView: RecyclerView,
        onItemClickUIAction: (songsToSend: Array<SongModel>) -> Unit
    ) {
        recyclerView.setHasFixedSize(true)

        recyclerView.adapter = optionsListAdapter

        optionsListAdapter.setOnItemClickListener(object: OptionsListAdapter.OnItemClickListener {
            override fun onItemClick(optionModel: OptionModel) {
                onItemClickUIAction(options[optionModel.id].toTypedArray())
            }
        })
    }

    fun addOptions() {
        val getThumbnail = getThumbnailFactory()
        val optionsList = options.mapIndexed { index, songs ->
            val firstOption = songs.first()
            OptionModel(index, firstOption.getParameterDisplayValue(selector), firstOption.getThumbnail(), songs.size)
        }

        optionsListAdapter.addItems(optionsList)
    }
}