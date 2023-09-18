package com.example.vibeslocal

import com.example.vibeslocal.services.SongsQueueService
import org.junit.Test

class SongsQueueServiceUnitTest {
    @Test
    fun testGettingSongFromEmptyQueue() {
        val service = SongsQueueService()
        assert(service.getCurrentSong() == (-1).toLong())

        service.clearQueue()
        assert(service.getCurrentSong() == (-1).toLong())

        service.setQueue(listOf<Long>())
        assert(service.getCurrentSong() == (-1).toLong())
    }

    @Test
    fun testSongQueuePlayback() {
        val service = SongsQueueService()
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