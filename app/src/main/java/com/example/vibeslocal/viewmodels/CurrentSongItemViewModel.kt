package com.example.vibeslocal.viewmodels

import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.example.vibeslocal.managers.SongThumbnailManager
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.services.MediaPlayerService
import java.lang.ref.WeakReference

class CurrentSongItemViewModel(private val songThumbnailManager: SongThumbnailManager): ViewModel() {
    var mediaPlayerService: WeakReference<MediaPlayerService> = WeakReference(null)

    fun subscribeToMediaPlayerEvent(event: MediaPlayerService.Events, action: (SongModel?) -> Unit) {
        mediaPlayerService.get()?.subscribeToEvent(event, action)
    }

    fun unsubscribeToMediaPlayerEvent(event: MediaPlayerService.Events, action: (SongModel?) -> Unit) {
        mediaPlayerService.get()?.unsubscribeToEvent(event, action)
    }

    fun loadSongItem(albumId: Long, songThumbnail: ImageView) {
        songThumbnail.setImageBitmap(songThumbnailManager.getThumbnail(albumId))
    }
}