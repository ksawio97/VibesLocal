package com.example.vibeslocal.models

fun <T> SongModel.getParameterDisplayValue(selector: (SongModel) -> T): String {
    return if (selector == SongModel::albumId) albumTitle else selector(this).toString()
}