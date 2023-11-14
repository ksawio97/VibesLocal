package com.example.vibeslocal.viewmodels

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.example.vibeslocal.managers.SongThumbnailManager
import com.example.vibeslocal.managers.SongsQueueManager
import com.example.vibeslocal.repositories.SongsRepository
import com.example.vibeslocal.services.MediaPlayerService
import java.lang.ref.WeakReference

class CurrentSongItemViewModel(
    private val songThumbnailManager: SongThumbnailManager,
    private val songsRepository: SongsRepository,
    private val songsQueueManager: SongsQueueManager): ViewModel() {
    var mediaPlayerService: WeakReference<MediaPlayerService> = WeakReference(null)

    fun loadSongItem(albumId: Long, songThumbnail: ImageView) {
        songThumbnail.setImageBitmap(songThumbnailManager.getThumbnail(albumId))
    }

    fun getCurrentSongId() = songsQueueManager.getCurrentSong()
    fun getSongById(id: Long) = songsRepository.getSongById(id)
}