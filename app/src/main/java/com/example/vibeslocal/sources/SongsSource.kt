package com.example.vibeslocal.sources

import android.content.ContentResolver
import android.content.ContentUris
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.provider.MediaStore
import android.util.Log
import com.example.vibeslocal.models.SongModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class SongsSource(private val contentResolver: ContentResolver) {
    suspend fun loadSongsData() : List<SongModel>? = coroutineScope {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA
        )
        val selection = null
        val selectionArgs = null
        val sortOrder = null

        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val filePathColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)

            val songModelListDeferred = ArrayList<Deferred<SongModel>>()
            if (cursor.moveToNext()) {
                Log.i("Debug", "Found ${cursor.count} songs")
                repeat(cursor.count) {
                    //load before async to avoid getting other songs data
                    val id = cursor.getLong(idColumn)
                    val title = cursor.getString(titleColumn)
                    val artist = cursor.getString(artistColumn)
                    val filePath = cursor.getString(filePathColumn)

                    val songModelDeferred = async(Dispatchers.IO) {
                        SongModel(id, title, artist, ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id), getSongThumbnail(filePath))
                    }
                    songModelListDeferred.add(songModelDeferred)
                    cursor.moveToNext()
                }
            }

            return@coroutineScope songModelListDeferred.awaitAll()
        }
        return@coroutineScope null
    }

    private fun getSongThumbnail(filePath: String): Bitmap? {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(filePath)

        val art = retriever.embeddedPicture
        retriever.release()
        if (art != null)
            return BitmapFactory.decodeByteArray(art, 0, art.size)
        return null
    }
}