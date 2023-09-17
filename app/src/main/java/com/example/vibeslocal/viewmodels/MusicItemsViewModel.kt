package com.example.vibeslocal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.adapters.MusicItemsListAdapter
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.repositories.SongsRepository
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.services.SongsQueueService
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class MusicItemsViewModel(private val songsRepository: SongsRepository, private val songsQueueService: SongsQueueService) : ViewModel(), KoinComponent {
    private lateinit var musicItemsListAdapter: MusicItemsListAdapter

    var mediaPlayerService: MediaPlayerService? = null
    var isBound = false

    fun ConfigureRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        musicItemsListAdapter = MusicItemsListAdapter(songsRepository.getAll().toMutableList())
        recyclerView.adapter = musicItemsListAdapter

        musicItemsListAdapter.setOnItemClickListener(object: MusicItemsListAdapter.OnItemClickListener {
            override fun onItemClick(songModel: SongModel?) {
                if(songModel == null)
                    return

                songsRepository.getSongById(songModel.id).let{
                    if (it == null)
                        return
                    val songs = songsRepository.getMappedNotNullSongs { song ->
                        if (song.id >= songModel.id)
                            return@getMappedNotNullSongs song.id
                        null
                    }

                    songsQueueService.setQueue(songs)
                }

                mediaPlayerService?.startPlayback()
            }
        })

    }

    fun loadDataToAdapter() {
        musicItemsListAdapter.setSongs(songsRepository.getAll())
        songsRepository.onSongsChanged {
            viewModelScope.launch {
                musicItemsListAdapter.setSongs(it)
            }
        }
    }
}