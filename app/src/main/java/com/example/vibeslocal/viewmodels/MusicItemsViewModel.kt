package com.example.vibeslocal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.adapters.MusicItemsListAdapter
import com.example.vibeslocal.managers.SongsQueueManager
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.repositories.SongsRepository
import com.example.vibeslocal.services.MediaPlayerService
import java.lang.ref.WeakReference

class MusicItemsViewModel(private val songsRepository: SongsRepository, private val songsQueueManager: SongsQueueManager) : ViewModel() {
    private val musicItemsListAdapter: MusicItemsListAdapter = MusicItemsListAdapter(mutableListOf())
    var mediaPlayerService: WeakReference<MediaPlayerService> = WeakReference(null)

    fun configureRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
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

                    songsQueueManager.setQueue(songs)
                }

                mediaPlayerService.get()?.startPlayback()
            }
        })

    }

    fun loadDataToAdapter(songs: Collection<SongModel>) {
        musicItemsListAdapter.setSongs(songs)
    }
}