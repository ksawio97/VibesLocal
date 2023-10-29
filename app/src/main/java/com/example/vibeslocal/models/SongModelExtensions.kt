package com.example.vibeslocal.models

import android.graphics.Bitmap
import com.example.vibeslocal.services.SongThumbnailService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

fun <T> SongModel.getParameterDisplayValue(selector: (SongModel) -> T): String {
    return if (selector == SongModel::albumId) albumTitle else selector(this).toString()
}

//implemented it as factory because if I want to load a lot of thumbnails i do not need koin to provide SongThumbnailService more than once
fun getThumbnailFactory(): SongModel.() -> Bitmap {
    val songThumbnailServiceHolder = object : KoinComponent {
        val songThumbnailService: SongThumbnailService by inject()
    }

    return fun SongModel.() : Bitmap {
        return songThumbnailServiceHolder.songThumbnailService.getThumbnail(albumId)
    }
}