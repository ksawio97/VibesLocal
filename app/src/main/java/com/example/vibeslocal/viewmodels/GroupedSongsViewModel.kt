package com.example.vibeslocal.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vibeslocal.adapters.MusicItemsListAdapter
import com.example.vibeslocal.managers.SongsQueueManager
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.services.MediaPlayerService
import java.lang.ref.WeakReference

class GroupedSongsViewModel(private val songsQueueManager: SongsQueueManager) : ViewModel() {
    var mediaPlayerService: WeakReference<MediaPlayerService> = WeakReference(null)

    /** Returns music item click handler
     * @param musicItemsViewModel weak reference to music item list adapter so it can based on it's items create queue
     * @return music item click handler witch sets songs queue
     */
    fun getMusicItemClickHandler(musicItemsViewModel: WeakReference<MusicItemsViewModel>) : MusicItemsListAdapter.OnItemClickListener {
        return object: MusicItemsListAdapter.OnItemClickListener {
            override fun onItemClick(songModel: SongModel) {
                //if can't retrieve songs do not go
                val musicItems = musicItemsViewModel.get().let {
                    if (it == null) {
                        Log.e(TAG, "Couldn't retrieve songs inside musicItemsViewModel")
                        return@onItemClick
                    }
                    it.getSongsFromAdapter()
                }
                //from this id get all other songs to queue
                var startId = musicItems.size
                //gets current song and next ones
                val newQueueSongsId = musicItems.mapIndexedNotNull { index, song ->
                    if (songModel.id == song.id)
                        startId = index
                    if (index >= startId)
                        return@mapIndexedNotNull song.id
                    null
                }
                songsQueueManager.setQueue(newQueueSongsId)

                mediaPlayerService.get()?.startPlayback()
            }
        }
    }

    companion object {
        const val TAG = "GroupedSongsViewModel"
    }
}