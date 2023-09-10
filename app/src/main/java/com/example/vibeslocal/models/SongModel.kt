package com.example.vibeslocal.models

import android.graphics.Bitmap
import android.net.Uri

//TODO hold thumbnail file path not whole thumbnail (potentially less resources used)
data class SongModel(val id: Long, val title: String, val artist: String, val uri: Uri, val thumbnail: Bitmap?)