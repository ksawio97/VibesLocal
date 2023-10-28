package com.example.vibeslocal.models

import android.net.Uri

data class SongModel(val id: Long, val title: String, val artist: String, val albumId: Long, val genre: String, val uri: Uri)