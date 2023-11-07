package com.example.vibeslocal.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.vibeslocal.generic.CustomEvent
import com.example.vibeslocal.managers.CustomEventManager
import com.example.vibeslocal.managers.ICustomEventManager
import com.example.vibeslocal.managers.SongsQueueManager
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.repositories.SongsRepository
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent

class MediaPlayerService : Service(), ICustomEventManager<MediaPlayerService.Events>, KoinComponent {
    private val binder = MediaPlayerBinder()
    private var mediaPlayer: MediaPlayer = MediaPlayer()

    private val songsQueueManager : SongsQueueManager by inject()
    private val songsRepository: SongsRepository by inject()

    private val isQueuePlaying = EventWithBooleanValue()
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand")
        return START_STICKY
    }

    fun startPlayback() {
        mediaPlayer.reset()
        playCurrentSong()
        customEventManager.notifyEvent(Events.CurrentSongChangedEvent, getCurrentSong())
        customEventManager.notifyEvent(Events.PauseChangedEvent, true)
        isQueuePlaying.setValue(true)
    }

    fun stopPlayback() {
        mediaPlayer.release()
        isQueuePlaying.setValue(false)
    }

    fun pausePlayback() {
        if (mediaPlayer.isPlaying)
            mediaPlayer.pause()
        else
            mediaPlayer.start()

        customEventManager.notifyEvent(Events.PauseChangedEvent, mediaPlayer.isPlaying)
    }
    fun isPlaying() : Boolean {
        return mediaPlayer.isPlaying
    }
    fun isQueuePlaying() : Boolean {
        return isQueuePlaying.getIsQueuePlaying()
    }

    fun getCurrentSong() : SongModel? {
        val currSongId = songsQueueManager.getCurrentSong()
        return songsRepository.getSongById(currSongId)
    }

    fun playPreviousSong() {
        if(songsQueueManager.goToPreviousSong()) {
            playCurrentSong()
            customEventManager.notifyEvent(Events.CurrentSongChangedEvent, getCurrentSong())
        }
    }

    fun playNextSong() {
        if(songsQueueManager.goToNextSong()) {
            playCurrentSong()
            customEventManager.notifyEvent(Events.CurrentSongChangedEvent, getCurrentSong())
        }
    }

    private fun playCurrentSong() {
        val currSong = getCurrentSong() ?: return

        mediaPlayer.reset()
        mediaPlayer.setDataSource(applicationContext, currSong.uri)
        mediaPlayer.prepareAsync()

        // Set a callback for when the media is prepared
        mediaPlayer.setOnPreparedListener { mp ->
            // Start playback
            Log.i(TAG, "Song ${currSong.title} just started")
            mp.start()
        }

        // Set a callback for when the media completes
        mediaPlayer.setOnCompletionListener { mp ->
            // Handle completion, e.g., play the next track
            Log.i(TAG, "Song ${currSong.title} just ended")
            playNextSong()
        }

        // Handle any errors
        mediaPlayer.setOnErrorListener { mp, what, extra ->
            Log.i(TAG, "Song ${currSong.title} had an error")
            isQueuePlaying.setValue(false)
            mp.reset()
            stopPlayback()
            false // Return false if you want to handle errors yourself
        }
    }

    inner class EventWithBooleanValue : CustomEvent<Boolean>() {
        private var value = false

        fun setValue(newValue: Boolean) {
            if(value == newValue)
                return
            value = newValue
            notify(value)
        }

        fun getIsQueuePlaying() : Boolean {
            return value
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
        mediaPlayer.release()
    }

    companion object {
        private const val TAG = "MediaPlayerService"
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
