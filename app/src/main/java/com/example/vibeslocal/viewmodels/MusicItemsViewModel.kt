package com.example.vibeslocal.viewmodels

import android.content.ContentResolver
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.services.AudioFilesService

class MusicItemsViewModel : ViewModel() {
    //TODO Inject it with Koin
    private val audioFilesService: AudioFilesService = AudioFilesService()

    fun loadData(contentResolver: ContentResolver) :Array<SongModel>? {
        Log.i("Debug", "MusicItemsViewModel loadData")
        return audioFilesService.getSongsData(contentResolver)
    }
}