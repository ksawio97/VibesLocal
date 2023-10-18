package com.example.vibeslocal.viewmodels

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.services.SongThumbnailService

class CurrentSongItemViewModel(private val songThumbnailService: SongThumbnailService): ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var mediaPlayerService: MediaPlayerService? = null

    fun subscribeToMediaPlayerEvent(event: MediaPlayerService.Events, action: (SongModel?) -> Unit) {
        mediaPlayerService?.subscribeToEvent(event, action)
    }

    fun unsubscribeToMediaPlayerEvent(event: MediaPlayerService.Events, action: (SongModel?) -> Unit) {
        mediaPlayerService?.unsubscribeToEvent(event, action)
    }

    fun loadSongItem(albumId: Long, songThumbnail: ImageView) {
        songThumbnail.setImageBitmap(songThumbnailService.getThumbnail(albumId))
    }
}