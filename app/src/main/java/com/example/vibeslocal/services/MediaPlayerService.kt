package com.example.vibeslocal.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder

class MediaPlayerService : Service() {
    private val binder = MediaPlayerBinder()
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    inner class MediaPlayerBinder : Binder() {
        fun getService() : MediaPlayerService{
            return this@MediaPlayerService
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    fun play(uri: Uri) {
        try {
            // Reset the MediaPlayer and set the data source
            mediaPlayer.reset()
            mediaPlayer.setDataSource(applicationContext, uri)

            // Prepare the MediaPlayer asynchronously
            mediaPlayer.prepareAsync()

            // Set a callback for when the media is prepared
            mediaPlayer.setOnPreparedListener { mp ->
                // Start playback
                mp.start()
            }

            // Set a callback for when the media completes
            mediaPlayer.setOnCompletionListener { mp ->
                // Handle completion, e.g., play the next track
            }

            // Handle any errors
            mediaPlayer.setOnErrorListener { mp, what, extra ->
                // Handle error cases
                false // Return false if you want to handle errors yourself
            }
        } catch (e: Exception) {
            // Handle exceptions
            e.printStackTrace()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}
