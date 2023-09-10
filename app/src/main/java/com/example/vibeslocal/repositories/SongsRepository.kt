package com.example.vibeslocal.repositories

import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.sources.SongsSource

//TODO maybe add observe method and change songs to observable data
class SongsRepository(private val songsSource: SongsSource) {
    private val songs: MutableList<SongModel> = mutableListOf()

    suspend fun loadData(){
        songs.clear()
        songsSource.loadSongsData().let{
            if (it != null)
                songs.addAll(it)
        }
    }

    fun getAll() : Collection<SongModel> {
        return songs
    }

    fun getSongById(id : Long): SongModel? {
        return songs.find { it.id == id}
    }

    fun <T> getMappedNotNullSongs(function: (song: SongModel) -> T?) : Collection<T> {
        return songs.mapNotNull(function)
    }
    fun <T> getMappedSongs(function: (song: SongModel) -> T) : Collection<T> {
        return songs.map(function)
    }
    fun getFilteredSongs(function: (song: SongModel) -> Boolean) : Collection<SongModel> {
        return songs.filter(function)
    }
}