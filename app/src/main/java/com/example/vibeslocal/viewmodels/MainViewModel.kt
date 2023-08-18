package com.example.vibeslocal.viewmodels

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.services.AudioFilesService

class MainViewModel : ViewModel(){
    private val audioFilesService: AudioFilesService = AudioFilesService()

    fun loadData(contentResolver: ContentResolver) : Array<SongModel>?{
        return audioFilesService.getSongsData(contentResolver)
    }
}