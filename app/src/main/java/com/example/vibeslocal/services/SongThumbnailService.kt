package com.example.vibeslocal.services

import android.app.Service
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.IBinder
import android.util.Log
import java.io.FileNotFoundException

class SongThumbnailService : Service() {
    private val loadedThumbnails : MutableMap<Long, Bitmap?> = mutableMapOf()
    private val albumArtUri: Uri = Uri.parse("content://media/external/audio/albumart")
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    fun putThumbnail(albumId: Long, contentResolver: ContentResolver) {
        if (loadedThumbnails.containsKey(albumId))
            return
        loadedThumbnails[albumId] = getAlbumArtByAlbumId(albumId, contentResolver)
    }

    fun getThumbnail(albumId: Long) : Bitmap? {
        return loadedThumbnails[albumId]
    }

    private fun getAlbumArtByAlbumId(albumId: Long, contentResolver: ContentResolver): Bitmap? {
        val albumArtUriWithAlbumId: Uri = ContentUris.withAppendedId(albumArtUri, albumId)

        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(albumArtUriWithAlbumId, "r")
            if (parcelFileDescriptor != null) {
                val albumArtBitmap = BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.fileDescriptor)
                parcelFileDescriptor.close()
                Log.i("Debug", "Loaded ${loadedThumbnails.size}th cover")
                return albumArtBitmap
            }
        } catch (e: FileNotFoundException) {
            //skip it when there's no art it happens
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }
}