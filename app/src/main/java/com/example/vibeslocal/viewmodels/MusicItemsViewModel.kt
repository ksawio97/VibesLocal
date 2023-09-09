package com.example.vibeslocal.viewmodels

import android.content.ContentResolver
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.services.AudioFilesService

class MusicItemsViewModel(private val audioFilesService: AudioFilesService) : ViewModel() {

    suspend fun loadData(contentResolver: ContentResolver): List<SongModel>? {
        Log.i("Debug", "MusicItemsViewModel loadData")
        return audioFilesService.getSongsData(contentResolver)
    }
}