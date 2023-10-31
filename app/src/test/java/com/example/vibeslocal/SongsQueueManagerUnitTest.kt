package com.example.vibeslocal

import com.example.vibeslocal.managers.SongsQueueManager
import org.junit.Test

class SongsQueueManagerUnitTest {
    @Test
    fun testGettingSongFromEmptyQueue() {
        val service = SongsQueueManager()
        assert(service.getCurrentSong() == (-1).toLong())

        service.clearQueue()
        assert(service.getCurrentSong() == (-1).toLong())

        service.setQueue(listOf<Long>())
        assert(service.getCurrentSong() == (-1).toLong())
    }

    @Test
    fun testSongQueuePlayback() {
        val service = SongsQueueManager()
        val songs = listOf<Long>(321, 3123232, 3234, 32478)
        service.setQueue(songs)

        assert(!service.goToPreviousSong())
        for (song in songs) {
            println("${service.getCurrentSong()} == $song")
            assert(service.getCurrentSong() == song)
            service.goToNextSong()
        }

        assert(service.getCurrentSong() == songs.last())
        assert(!service.goToNextSong())

        for (song in songs.reversed()) {
            assert(service.getCurrentSong() == song)
            service.goToPreviousSong()
        }

        assert(service.getCurrentSong() == songs.first())
    }
}