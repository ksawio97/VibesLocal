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

    fun getAll() : List<SongModel> {
        return songs
    }
}