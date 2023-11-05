package com.example.vibeslocal.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.vibeslocal.R
import com.example.vibeslocal.databinding.SongItemBinding
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.viewmodels.CurrentSongItemViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ref.WeakReference

class CurrentSongItemFragment : Fragment(R.layout.song_item) {
    private val viewModel : CurrentSongItemViewModel by viewModel()
    private lateinit var binding: SongItemBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SongItemBinding.bind(view)

        val onCurrentSongChangedEvent: (SongModel?) -> Unit = { model ->
            if (model != null)
            {
                val songTitle : TextView = binding.songTitle
                val songAuthor : TextView = binding.songAuthor
                val songThumbnail : ImageView = binding.songThumbnail

                songTitle.text = model.title
                songAuthor.text = model.artist

                viewModel.loadSongItem(model.albumId, songThumbnail)
            }
        }
        //handle connection
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as MediaPlayerService.MediaPlayerBinder

                val mediaPlayerService = binder.getService()
                viewModel.mediaPlayerService = WeakReference(mediaPlayerService)
                onCurrentSongChangedEvent(mediaPlayerService.getCurrentSong())

                viewModel.subscribeToMediaPlayerEvent(MediaPlayerService.Events.CurrentSongChangedEvent, onCurrentSongChangedEvent)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                viewModel.mediaPlayerService.clear()
                viewModel.unsubscribeToMediaPlayerEvent(MediaPlayerService.Events.CurrentSongChangedEvent, onCurrentSongChangedEvent)
            }
        }

        //connect to MediaPlayerService
        val intent = Intent(requireContext(), MediaPlayerService::class.java)
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }
}