package com.example.vibeslocal.repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.vibeslocal.generic.CustomEvent
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.sources.SongsSource

class SongsRepository(private val songsSource: SongsSource) {
    private val songs: MutableList<SongModel> = mutableListOf()
    private val songsChangedEvent = CustomEvent<Collection<SongModel>>()

    @RequiresApi(Build.VERSION_CODES.R)
    suspend fun loadData(){
        songs.clear()
        val songsData = songsSource.loadSongsData() ?: return
        songs.addAll(songsData)
        songsChangedEvent.notify(songs)
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

    fun <T> getGroupedSongs(selector: (SongModel) -> T) : Map<T, List<SongModel>> {
        return songs.groupBy(selector)
    }
    fun onSongsChanged(action: (Collection<SongModel>) -> Unit) {
        songsChangedEvent.subscribe(action)
    }
}