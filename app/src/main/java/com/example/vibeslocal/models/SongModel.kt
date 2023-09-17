package com.example.vibeslocal.models

import android.graphics.Bitmap
import android.net.Uri

//TODO hold thumbnail in different model (if there's album downloaded a lot of songs use same thumbnail)
data class SongModel(val id: Long, val title: String, val artist: String, val uri: Uri, val thumbnail: Bitmap?)