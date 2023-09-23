package com.example.vibeslocal.viewmodels

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import com.example.vibeslocal.services.MediaPlayerService

class PlaybackControlViewModel() : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var mediaPlayerService: MediaPlayerService? = null

    fun pauseSong() {
        mediaPlayerService?.pausePlayback()
    }

    fun previousSong() {
        mediaPlayerService?.playPreviousSong()
    }

    fun nextSong() {
        mediaPlayerService?.playNextSong()
    }
}