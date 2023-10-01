package com.example.vibeslocal.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.vibeslocal.generic.CustomEvent
import com.example.vibeslocal.generic.CustomEventManager
import com.example.vibeslocal.generic.ICustomEventManagerClass
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.repositories.SongsRepository
import org.koin.android.ext.android.inject

class MediaPlayerService : Service(), ICustomEventManagerClass<MediaPlayerService.Events> {
    private val binder = MediaPlayerBinder()
    private var mediaPlayer: MediaPlayer = MediaPlayer()

    private val songsQueueService : SongsQueueService by inject()
    private val songsRepository: SongsRepository by inject()

    private val isQueuePlaying = IsQueuePlaying()
    private val customEventManager: CustomEventManager<Events> =
        CustomEventManager(mapOf(
            Pair(Events.PauseChangedEvent,  CustomEvent()),
            Pair(Events.IsQueuePlayingChangedEvent, isQueuePlaying),
            Pair(Events.CurrentSongChangedEvent, CustomEvent()))
        )

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
        customEventManager.notifyEvent(Events.CurrentSongChangedEvent, getCurrentSong())
        customEventManager.notifyEvent(Events.PauseChangedEvent, true)
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

        customEventManager.notifyEvent(Events.PauseChangedEvent, mediaPlayer.isPlaying)
    }

    fun getCurrentSong() : SongModel? {
        val currSongId = songsQueueService.getCurrentSong()
        return songsRepository.getSongById(currSongId)
    }

    fun playPreviousSong() {
        if(songsQueueService.goToPreviousSong()) {
            playCurrentSong()
            customEventManager.notifyEvent(Events.CurrentSongChangedEvent, getCurrentSong())
        }
    }

    fun playNextSong() {
        if(songsQueueService.goToNextSong()) {
            playCurrentSong()
            customEventManager.notifyEvent(Events.CurrentSongChangedEvent, getCurrentSong())
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
                isQueuePlaying.setQueuePlaying(false)
                mp.reset()
                stopPlayback()
                false // Return false if you want to handle errors yourself
            }
        }
    }

    inner class IsQueuePlaying : CustomEvent<Boolean>() {
        private var isQueuePlaying = false

        fun setQueuePlaying(newValue: Boolean) {
            if(isQueuePlaying == newValue)
                return
            isQueuePlaying = newValue
            customEventManager.notifyEvent(Events.IsQueuePlayingChangedEvent, isQueuePlaying)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    companion object {
        const val TAG = "MediaPlayerService"
    }

    enum class Events {
        IsQueuePlayingChangedEvent,
        PauseChangedEvent,
        CurrentSongChangedEvent
    }

    override fun <T> subscribeToEvent(event: Events, action: (T) -> Unit) {
        customEventManager.subscribeToEvent(event, action)
    }

    override fun <T> unsubscribeToEvent(event: Events, action: (T) -> Unit) {
        customEventManager.unsubscribeToEvent(event, action)
    }
}
