package com.example.vibeslocal.viewmodels

import android.content.ContentResolver
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.services.AudioFilesService

class MusicItemsViewModel(private val audioFilesService: AudioFilesService) : ViewModel() {

    fun loadData(contentResolver: ContentResolver, addSong: (newSong: SongModel) -> Unit) {
        Log.i("Debug", "MusicItemsViewModel loadData")
        audioFilesService.getSongsData(contentResolver).forEach { song ->
            addSong(song)
        }
    }
}