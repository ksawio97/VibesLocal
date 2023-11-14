package com.example.vibeslocal.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.vibeslocal.events.CustomEvent
import com.example.vibeslocal.events.ICustomEventClass
import com.example.vibeslocal.managers.SongsQueueManager
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.repositories.SongsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent

class MediaPlayerService : Service(), KoinComponent {
    private val binder = MediaPlayerBinder()
    private var mediaPlayer: MediaPlayer = MediaPlayer()

    private val songsQueueManager : SongsQueueManager by inject()
    private val songsRepository: SongsRepository by inject()

    private val isQueuePlaying = EventWithBooleanValue()
    private val pauseChanged = CustomEvent<Boolean>()
    private val currentSongChanged = CustomEvent<Long>()

    //#region public events handles
    val isQueuePlayingEvent: ICustomEventClass<Boolean>
        get() = isQueuePlaying
    val pauseChangedEvent: ICustomEventClass<Boolean>
        get() = pauseChanged
    val currentSongChangedEvent: ICustomEventClass<Long>
        get() = currentSongChanged
    //#endregion

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

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
        mediaPlayer.release()
    }

    fun getPlaybackProgressFlow(): Flow<Int> = flow {
        while (true) {
            emit(mediaPlayer.currentPosition)
            delay(500)
        }
    }

    fun getPlaybackDuration(): Int = mediaPlayer.duration

    fun startPlayback() {
        playCurrentSong()
        currentSongChanged.notify(songsQueueManager.getCurrentSong())
        pauseChanged.notify(true)
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
        pauseChanged.notify(mediaPlayer.isPlaying)
    }
    fun isPlaying() : Boolean {
        return mediaPlayer.isPlaying
    }
    fun isQueuePlaying() : Boolean {
        return isQueuePlaying.getIsQueuePlaying()
    }

    private fun getCurrentSong() : SongModel? {
        val currSongId = songsQueueManager.getCurrentSong()
        return songsRepository.getSongById(currSongId)
    }

    fun playPreviousSong() {
        if(songsQueueManager.goToPreviousSong()) {
            playCurrentSong()
            currentSongChanged.notify(songsQueueManager.getCurrentSong())
        }
    }

    fun playNextSong() {
        if(songsQueueManager.goToNextSong()) {
            playCurrentSong()
            currentSongChanged.notify(songsQueueManager.getCurrentSong())
        }
    }

    private fun playCurrentSong() {
        val currSong = getCurrentSong() ?: return

        mediaPlayer.reset()
        mediaPlayer.setDataSource(applicationContext, currSong.uri)
        mediaPlayer.prepare()

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

    companion object {
        private const val TAG = "MediaPlayerService"
    }
}
