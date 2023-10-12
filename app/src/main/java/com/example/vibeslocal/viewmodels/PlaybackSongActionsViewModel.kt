package com.example.vibeslocal.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.example.vibeslocal.services.MediaPlayerService

class PlaybackSongActionsViewModel : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var mediaPlayerService : MediaPlayerService? = null

    fun playPrevious() {
        mediaPlayerService?.playPreviousSong()
    }

    fun pause() {
        mediaPlayerService?.pausePlayback()
    }

    fun playNext() {
        mediaPlayerService?.playNextSong()
    }

    fun subscribeToMediaPlayerEvent(event: MediaPlayerService.Events, action: (Boolean) -> Unit) {
        mediaPlayerService?.subscribeToEvent(event, action)
    }

    fun unsubscribeToMediaPlayerEvent(event: MediaPlayerService.Events, action: (Boolean) -> Unit) {
        mediaPlayerService?.unsubscribeToEvent(event, action)
    }
}