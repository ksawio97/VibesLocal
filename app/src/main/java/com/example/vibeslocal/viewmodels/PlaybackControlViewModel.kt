package com.example.vibeslocal.viewmodels

import androidx.lifecycle.ViewModel
import com.example.vibeslocal.services.MediaPlayerService
import java.lang.ref.WeakReference

class PlaybackControlViewModel : ViewModel() {
    //TODO handle service connection here
    var mediaPlayerService: WeakReference<MediaPlayerService> = WeakReference(null)

    fun pauseSong() {
        mediaPlayerService.get()?.pausePlayback()
    }

    fun subscribeToMediaPlayerEvent(event: MediaPlayerService.Events, action: (Boolean) -> Unit) {
        mediaPlayerService.get()?.subscribeToEvent(event, action)
    }

    fun unsubscribeToMediaPlayerEvent(event: MediaPlayerService.Events, action: (Boolean) -> Unit) {
        mediaPlayerService.get()?.unsubscribeToEvent(event, action)
    }
}