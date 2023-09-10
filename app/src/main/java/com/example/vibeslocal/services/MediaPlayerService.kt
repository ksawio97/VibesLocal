package com.example.vibeslocal.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.repositories.SongsRepository
import org.koin.android.ext.android.inject

class MediaPlayerService() : Service() {
    private val binder = MediaPlayerBinder()
    private var mediaPlayer: MediaPlayer = MediaPlayer()

    private val songsQueueService : SongsQueueService by inject()
    private val songsRepository: SongsRepository by inject()
    inner class MediaPlayerBinder : Binder() {
        fun getService() : MediaPlayerService{
            return this@MediaPlayerService
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    fun startPlayback() {
        mediaPlayer.reset()
        playCurrentSong()
    }

    fun stopPlayback() {
        mediaPlayer.release()
    }

    fun getCurrentSong() : SongModel? {
        val currSongId = songsQueueService.getCurrentSong()
        return songsRepository.getSongById(currSongId)
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
                if (songsQueueService.goToNextSong())
                    playCurrentSong()
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
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    companion object {
        const val TAG = "MediaPlayerService"
    }
}
