package com.example.vibeslocal.services

import android.app.Service
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.IBinder
import android.util.Log
import com.example.vibeslocal.R
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.FileNotFoundException

class SongThumbnailService : Service(), KoinComponent {
    private val loadedThumbnails : MutableMap<Long, Bitmap?> = mutableMapOf()

    private val defaultThumbnail : Bitmap by lazy {
        val context: Context by inject()

        BitmapFactory.decodeResource(context.resources, R.drawable.unknown)
    }

    private val albumArtUri: Uri = Uri.parse("content://media/external/audio/albumart")
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    fun putThumbnail(albumId: Long, contentResolver: ContentResolver) {
        if (loadedThumbnails.containsKey(albumId))
            return
        loadedThumbnails[albumId] = getAlbumArtByAlbumId(albumId, contentResolver)
    }

    fun getThumbnail(albumId: Long) : Bitmap {
        return loadedThumbnails[albumId] ?: defaultThumbnail
    }

    private fun getAlbumArtByAlbumId(albumId: Long, contentResolver: ContentResolver): Bitmap? {
        val albumArtUriWithAlbumId: Uri = ContentUris.withAppendedId(albumArtUri, albumId)

        try {
            val parcelFileDescriptor = contentResolver.openFileDescriptor(albumArtUriWithAlbumId, "r")
            if (parcelFileDescriptor != null) {
                val albumArtBitmap = BitmapFactory.decodeFileDescriptor(parcelFileDescriptor.fileDescriptor)
                parcelFileDescriptor.close()
                Log.i(TAG, "Loaded ${loadedThumbnails.size}th cover")
                return albumArtBitmap
            }
        } catch (e: FileNotFoundException) {
            //skip it when there's no art it happens
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    companion object {
        const val TAG = "SongThumbnailService"
    }
}