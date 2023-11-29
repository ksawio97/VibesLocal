package com.example.vibeslocal.viewmodels

import androidx.lifecycle.ViewModel
import com.example.vibeslocal.services.MediaPlayerService
import java.lang.ref.WeakReference

class PlaybackControl2ViewModel : ViewModel() {
    var mediaPlayerService: WeakReference<MediaPlayerService> = WeakReference(null)

    fun playPrevious() {
        mediaPlayerService.get()?.playPreviousSong()
    }

    fun pause() {
        mediaPlayerService.get()?.pausePlayback()
    }

    fun playNext() {
        mediaPlayerService.get()?.playNextSong()
    }
}