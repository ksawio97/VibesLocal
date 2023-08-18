package com.example.vibeslocal.services

import android.app.Service
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.os.Binder
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import com.example.vibeslocal.models.SongModel

class AudioFilesService : Service() {
    private val binder = AudioFilesBinder()

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class AudioFilesBinder : Binder() {
        fun getService(): AudioFilesService {
            return this@AudioFilesService
        }
    }

    //TODO make this function load data and add it incrementally (with no need to wait for all elements)
    fun getSongsData(contentResolver: ContentResolver) : Array<SongModel>?{
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA
            )
        val selection = null
        val selectionArgs = null
        val sortOrder = "${MediaStore.Audio.Media.TITLE} ASC"

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

            if (cursor.moveToNext()) {
                Log.i("Debug", "Found ${cursor.count} songs")
                //return songs array
                return Array<SongModel>(cursor.count) {
                    val song = SongModel(cursor.getLong(idColumn), cursor.getString(titleColumn), cursor.getString(artistColumn), getSongThumbnail(cursor.getString(filePathColumn)))
                    cursor.moveToNext()
                    song
                }
            }
        }
        return null
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