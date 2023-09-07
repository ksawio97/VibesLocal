package com.example.vibeslocal.viewmodels

import android.content.ContentResolver
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.services.AudioFilesService

class MusicItemsViewModel(private val audioFilesService: AudioFilesService) : ViewModel() {

    //TODO save data into adapter incrementally (when read 1 song add it instantly while reading more)
    fun loadData(contentResolver: ContentResolver) : Array<SongModel> {
        Log.i("Debug", "MusicItemsViewModel loadData")
        return audioFilesService.getSongsData(contentResolver).toList().toTypedArray()
    }
}