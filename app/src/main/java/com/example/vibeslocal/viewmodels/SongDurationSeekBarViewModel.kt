package com.example.vibeslocal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibeslocal.services.MediaPlayerService
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

class SongDurationSeekBarViewModel : ViewModel() {
    var mediaPlayerService: WeakReference<MediaPlayerService> = WeakReference(null)
    private var updateJob: Job? = null
    var suspenseUpdating: Boolean = false

    fun startUpdatingProgressBar(progressBarSetter: (Int) -> Unit) {
        updateJob = viewModelScope.launch {
            mediaPlayerService.get()?.getPlaybackProgressFlow()?.collect { position ->
                if (!suspenseUpdating)
                    progressBarSetter(position)
            }
        }
    }

    fun setPlaybackProgress(newProgress: Int) {
        mediaPlayerService.get()?.setPlaybackProgress(newProgress)
    }
    fun getPlaybackDuration(): Int = mediaPlayerService.get()?.getPlaybackDuration() ?: 1

    override fun onCleared() {
        super.onCleared()
        updateJob?.cancel()
    }
}