package com.example.vibeslocal.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class SongsQueueService : Service() {
    private val songsQueue = mutableListOf<Long>()
    private var currentSong: Int = -1
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    fun setQueue(songs: Collection<Long>) {
        songsQueue.clear()
        currentSong = 0
        songsQueue.addAll(songs)
    }

    fun clearQueue() {
        songsQueue.clear()
        currentSong = -1
    }

    fun getCurrentSong() : Long {
        return if (songsQueue.size > currentSong) songsQueue[currentSong] else -1
    }

    fun goToNextSong() : Boolean{
        if (songsQueue.size > currentSong + 1) {
            currentSong += 1
            return true
        }
        return false
    }

    fun goToPreviousSong() : Boolean{
        if (currentSong > 0) {
            currentSong -= 1
            return true
        }
        return false
    }
}