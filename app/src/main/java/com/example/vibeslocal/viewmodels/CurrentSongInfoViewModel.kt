package com.example.vibeslocal.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.vibeslocal.managers.SongThumbnailManager
import com.example.vibeslocal.managers.SongsQueueManager
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.repositories.SongsRepository

class CurrentSongInfoViewModel
    (private val thumbnailManager: SongThumbnailManager,
     private val songsRepository: SongsRepository,
     private val songsQueueManager: SongsQueueManager) : ViewModel() {

    fun getSongInfo(songId: Long): SongModel? = songsRepository.getSongById(songId)

    fun getSongThumbnail(songId: Long): Bitmap = thumbnailManager.getThumbnail(songId)

    fun getCurrentSongId(): Long = songsQueueManager.getCurrentSong()
}