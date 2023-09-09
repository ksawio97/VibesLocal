package com.example.vibeslocal.models

import android.graphics.Bitmap

//TODO hold thumbnail file path not whole thumbnail (potentially less resources used)
data class SongModel(val id: Long, val title: String, val artist: String, val thumbnail: Bitmap?)