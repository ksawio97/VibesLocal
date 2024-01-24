package com.example.vibeslocal.viewmodels

import androidx.lifecycle.ViewModel
import com.example.vibeslocal.managers.SongsQueueManager
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.repositories.SongsRepository

class QueueInfoViewModel(
    private val songsQueueManager: SongsQueueManager,
    private val songsRepository: SongsRepository) : ViewModel() {
    fun getQueueSongs() : Collection<SongModel> {
        //index is a order in queue
        val songsQueue = songsQueueManager.getSongsQueue().mapIndexed { index, id -> id to index }.toMap()

        val songs = MutableList<SongModel?>(songsQueue.size) { null }
        songsRepository.getAll().forEach { song ->
            if (song.id in songsQueue) {
                //index always will be in range of array bcs it's length is the same as songsQueue
                val index = songsQueue[song.id]!!
                songs[index] = song
            }
        }
        return songs.filterNotNull()
    }
}