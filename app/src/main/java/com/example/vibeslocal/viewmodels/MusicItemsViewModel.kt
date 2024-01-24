package com.example.vibeslocal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.adapters.MusicItemsListAdapter
import com.example.vibeslocal.managers.SongsQueueManager
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.repositories.SongsRepository
import com.example.vibeslocal.services.MediaPlayerService
import java.lang.ref.WeakReference

class MusicItemsViewModel(private val songsQueueManager: SongsQueueManager) : ViewModel() {
    private val musicItemsListAdapter: MusicItemsListAdapter = MusicItemsListAdapter(mutableListOf())
    var mediaPlayerService: WeakReference<MediaPlayerService> = WeakReference(null)

    fun configureRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = musicItemsListAdapter

        musicItemsListAdapter.setOnItemClickListener(object: MusicItemsListAdapter.OnItemClickListener {
            override fun onItemClick(songModel: SongModel?) {
                if(songModel == null)
                    return
                //TODO this view model is used in SongQueueInfo, this setOnItemClickListener must be declared somewhere else
                val musicItemsSongs = musicItemsListAdapter.getSongs()
                //from this id get all other songs to queue
                var startId = musicItemsSongs.size
                //gets current song and next ones
                val newQueueSongsId = musicItemsSongs.mapIndexedNotNull { index, song ->
                    if (songModel.id == song.id)
                        startId = index
                    if (index >= startId)
                        return@mapIndexedNotNull song.id
                    null
                }
                songsQueueManager.setQueue(newQueueSongsId)

                mediaPlayerService.get()?.startPlayback()
            }
        })

    }

    fun loadDataToAdapter(songs: Collection<SongModel>) {
        musicItemsListAdapter.setSongs(songs)
    }
}