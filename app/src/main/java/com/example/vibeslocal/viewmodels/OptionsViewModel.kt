package com.example.vibeslocal.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.adapters.OptionsListAdapter
import com.example.vibeslocal.managers.SongThumbnailManager
import com.example.vibeslocal.models.OptionModel
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.models.getParameterDisplayValue
import com.example.vibeslocal.repositories.SongsRepository

class OptionsViewModel(private val songsRepository: SongsRepository, private val songThumbnailManager: SongThumbnailManager) : ViewModel() {
    private lateinit var options : Map<Any, OptionsHolder>
    private lateinit var selector : (SongModel) -> Any?
    private val optionsListAdapter = OptionsListAdapter()

    inner class OptionsHolder(val optionThumbnailId: Long, val songs: List<SongModel>)

    fun <T> loadGroupedSongs(selector: (SongModel) -> T) {
        options = songsRepository.getGroupedSongs(selector).map { songsGroup ->
            songsGroup.key as Any to
                    OptionsHolder(
                        songsGroup.value.first().albumId,
                        songsGroup.value)
        }.toMap()

        this.selector = selector
    }
    fun configureRecyclerView(
        recyclerView: RecyclerView,
        onItemClickUIAction: (optionModel: OptionModel,
                              songsToSend: Collection<SongModel>,
                              optionThumbnailId: Long) -> Unit
    ) {
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = optionsListAdapter

        optionsListAdapter.setOnItemClickListener(object: OptionsListAdapter.OnItemClickListener {
            override fun onItemClick(optionModel: OptionModel) {
                val option = options[optionModel.key]
                if (option != null) {
                    onItemClickUIAction(optionModel, option.songs, option.optionThumbnailId)
                }
                else {
                    Log.e(TAG, "Option with key ${optionModel.key} doesn't exist")
                }
            }
        })
    }

    fun addOptions() {
        val optionsList = options.map { optionSongsHolder ->
            OptionModel(optionSongsHolder.key,
                optionSongsHolder.value.songs.first().getParameterDisplayValue(selector),
                songThumbnailManager.getThumbnail(optionSongsHolder.value.optionThumbnailId),
                optionSongsHolder.value.songs.size)
        }
        optionsListAdapter.addItems(optionsList)
    }

    companion object {
        const val TAG = "OptionsViewModel"
    }
}