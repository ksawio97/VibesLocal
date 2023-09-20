package com.example.vibeslocal.sources

import android.content.ContentResolver
import android.content.ContentUris
import android.provider.MediaStore
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.services.SongThumbnailService
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class SongsSource(private val contentResolver: ContentResolver, private val songThumbnailService: SongThumbnailService) {
    suspend fun loadSongsData() : List<SongModel>? = coroutineScope {

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM_ID
        )

        contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)


            val loadedAlbumsThumbnails = mutableSetOf<Long>()
            val songModels = ArrayList<SongModel>()
            val deferredThumbnails = ArrayList<Deferred<Unit>>()
            if (cursor.moveToNext()) {
                repeat(cursor.count) {
                    //load before async to avoid getting other songs data
                    val id = cursor.getLong(idColumn)
                    val title = cursor.getString(titleColumn)
                    val artist = cursor.getString(artistColumn)
                    val albumId = cursor.getLong(albumIdColumn)

                    deferredThumbnails.add(async(Dispatchers.IO) {
                        if(loadedAlbumsThumbnails.contains(albumId))
                            return@async
                        loadedAlbumsThumbnails.add(albumId)
                        songThumbnailService.putThumbnail(albumId, contentResolver)
                    })
                    songModels.add(SongModel(id, title, artist, albumId, ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id)))

                    cursor.moveToNext()
                }
            }
            //wait for thumbnails to load
            deferredThumbnails.awaitAll()
            return@coroutineScope songModels
        }
        return@coroutineScope null
    }
}