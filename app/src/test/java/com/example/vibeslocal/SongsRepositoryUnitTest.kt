package com.example.vibeslocal

import android.net.Uri
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.repositories.SongsRepository
import com.example.vibeslocal.sources.SongsSource
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SongsRepositoryUnitTest {
    @MockK
    lateinit var songsSource : SongsSource

    @MockK
    lateinit var testUri: Uri

    @Before
    fun setUp() {
        songsSource = mockk()
        testUri = mockk()
    }
    @Test
    fun testGetAll() = runBlocking {
        coEvery  { songsSource.loadSongsData() } returns null
        val songsRepository = SongsRepository(songsSource).apply {
            loadData()
        }

        assertTrue(songsRepository.getAll().isEmpty())

        val returnObject = listOf(SongModel(321, "title1", "artist1", 2, testUri),
                            SongModel(423, "title2", "artist2", 2, testUri),
                            SongModel(5233, "title3", "artist3", 3, testUri))
        coEvery  { songsSource.loadSongsData() } returns returnObject
        songsRepository.loadData()

        val returned = songsRepository.getAll()
        assertNotNull(returned)
        assertEquals(returnObject, returned)
    }

    @Test
    fun testOnSongsChanged() = runBlocking {
        var expectedLoadedData: Collection<SongModel> = emptyList()
        coEvery  { songsSource.loadSongsData() } returns null
        val songsRepository = SongsRepository(songsSource).apply {
            onSongsChanged { loadedData ->
                assertEquals(loadedData, expectedLoadedData)
            }
        }

        songsRepository.loadData()

        expectedLoadedData = listOf(SongModel(321, "title1", "artist1", 2, testUri),
            SongModel(423, "title2", "artist2", 2, testUri),
            SongModel(5233, "title3", "artist3", 3, testUri))
        coEvery  { songsSource.loadSongsData() } returns expectedLoadedData.toList()
        songsRepository.loadData()
    }
}