package com.example.vibeslocal.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.vibeslocal.generic.CustomEvent
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.repositories.SongsRepository
import org.koin.android.ext.android.inject

class MediaPlayerService : Service() {
    private val binder = MediaPlayerBinder()
    private var mediaPlayer: MediaPlayer = MediaPlayer()

    private val songsQueueService : SongsQueueService by inject()
    private val songsRepository: SongsRepository by inject()

    private val pauseChangedEvent: CustomEvent<Boolean> = CustomEvent()

    private val isQueuePlaying = IsQueuePlaying()

    inner class MediaPlayerBinder : Binder() {
        fun getService() : MediaPlayerService{
            return this@MediaPlayerService
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    fun startPlayback() {
        mediaPlayer.reset()
        playCurrentSong()
        isQueuePlaying.setQueuePlaying(true)
    }

    fun stopPlayback() {
        mediaPlayer.release()
        isQueuePlaying.setQueuePlaying(false)
    }

    fun pausePlayback() {
        if (mediaPlayer.isPlaying)
            mediaPlayer.pause()
        else
            mediaPlayer.start()

        pauseChangedEvent.notify(mediaPlayer.isPlaying)
    }

    fun getCurrentSong() : SongModel? {
        val currSongId = songsQueueService.getCurrentSong()
        return songsRepository.getSongById(currSongId)
    }

    fun playPreviousSong() {
        if(songsQueueService.goToPreviousSong())
            playCurrentSong()
    }

    fun playNextSong() {
        if(songsQueueService.goToNextSong())
            playCurrentSong()
        isQueuePlaying.setQueuePlaying(true)
    }

    fun subscribeToEvent(event: Events, action: (Boolean) -> Unit) {
        when (event) {
            Events.pauseChangedEvent -> pauseChangedEvent.subscribe(action)
            Events.isQueuePlayingChangedEvent -> isQueuePlaying.subscribe(action)
        }
    }

    fun unsubscribeToEvent(event: Events, action: (Boolean) -> Unit) {
        when (event) {
            Events.pauseChangedEvent -> pauseChangedEvent.unsubscribe(action)
            Events.isQueuePlayingChangedEvent -> isQueuePlaying.unsubscribe(action)
        }
    }

    private fun playCurrentSong() {
        getCurrentSong().let {
            if(it == null)
                return@playCurrentSong
            mediaPlayer.reset()
            mediaPlayer.setDataSource(applicationContext, it.uri)
            mediaPlayer.prepareAsync()

            // Set a callback for when the media is prepared
            mediaPlayer.setOnPreparedListener { mp ->
                // Start playback
                Log.i(TAG, "Song ${it.title} just started")
                mp.start()
            }

            // Set a callback for when the media completes
            mediaPlayer.setOnCompletionListener { mp ->
                // Handle completion, e.g., play the next track
                Log.i(TAG, "Song ${it.title} just ended")
                playNextSong()
            }

            // Handle any errors
            mediaPlayer.setOnErrorListener { mp, what, extra ->
                Log.i(TAG, "Song ${it.title} had an error")
                mp.reset()
                stopPlayback()
                false // Return false if you want to handle errors yourself
            }
        }
    }

    enum class Events {
        isQueuePlayingChangedEvent,
        pauseChangedEvent
    }

    inner class IsQueuePlaying : CustomEvent<Boolean>() {
        private var isQueuePlaying = false

        fun setQueuePlaying(newValue: Boolean) {
            if(isQueuePlaying == newValue)
                return
            isQueuePlaying = newValue
            notify(isQueuePlaying)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    companion object {
        const val TAG = "MediaPlayerService"
    }
}
