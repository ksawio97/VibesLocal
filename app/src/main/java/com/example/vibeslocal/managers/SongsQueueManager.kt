package com.example.vibeslocal.managers

class SongsQueueManager {
    private val songsQueue = mutableListOf<Long>()
    private var currentSong: Int = -1

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
        return if (songsQueue.size > currentSong && currentSong >= 0) songsQueue[currentSong] else -1
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

    /**
     * Returns Queue with songs id's
     * @return Songs Queue copy
     */
    fun getSongsQueue() : Collection<Long> {
        return songsQueue.toList()
    }
}