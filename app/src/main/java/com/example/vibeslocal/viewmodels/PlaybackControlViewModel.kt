package com.example.vibeslocal.viewmodels

import androidx.lifecycle.ViewModel
import com.example.vibeslocal.services.MediaPlayerService

class PlaybackControlViewModel() : ViewModel() {
    var mediaPlayerService: MediaPlayerService? = null

    fun pauseSong() {
        mediaPlayerService?.pausePlayback()
    }

    fun subscribeToMediaPlayerEvent(event: MediaPlayerService.Events, action: (Boolean) -> Unit) {
        mediaPlayerService?.subscribeToEvent(event, action)
    }

    fun unsubscribeToMediaPlayerEvent(event: MediaPlayerService.Events, action: (Boolean) -> Unit) {
        mediaPlayerService?.unsubscribeToEvent(event, action)
    }
}