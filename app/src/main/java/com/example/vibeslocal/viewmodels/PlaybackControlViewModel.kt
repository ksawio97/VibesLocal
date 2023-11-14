package com.example.vibeslocal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibeslocal.services.MediaPlayerService
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class PlaybackControlViewModel : ViewModel() {
    var mediaPlayerService: WeakReference<MediaPlayerService> = WeakReference(null)
    private var updateJob: Job? = null

    fun pauseSong() {
        mediaPlayerService.get()?.pausePlayback()
    }

    fun getPlaybackDuration(): Int = mediaPlayerService.get()?.getPlaybackDuration() ?: 1

    fun startUpdatingProgressBar(progressBarSetter: (Int) -> Unit) {
        updateJob = viewModelScope.launch {
            mediaPlayerService.get()?.getPlaybackProgressFlow()?.collect { position ->
                progressBarSetter(position)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        updateJob?.cancel()
    }
}