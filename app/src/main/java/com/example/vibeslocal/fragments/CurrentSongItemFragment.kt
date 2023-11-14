package com.example.vibeslocal.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.fragment.app.Fragment
import com.example.vibeslocal.R
import com.example.vibeslocal.databinding.SongItemBinding
import com.example.vibeslocal.events.ServiceConnectionWithEventManager
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.viewmodels.CurrentSongItemViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ref.WeakReference

class CurrentSongItemFragment : Fragment(R.layout.song_item) {
    private val viewModel : CurrentSongItemViewModel by viewModel()
    private lateinit var binding: SongItemBinding
    private lateinit var serviceConnection: ServiceConnectionWithEventManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = SongItemBinding.bind(view)

        //handle connection
        serviceConnection = object : ServiceConnectionWithEventManager() {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as MediaPlayerService.MediaPlayerBinder

                val mediaPlayerService = binder.getService()
                viewModel.mediaPlayerService = WeakReference(mediaPlayerService)

                updateCurrSongData(viewModel.getCurrentSongId())
                mediaPlayerService.currentSongChangedEvent.let {
                    eventManager.subscribeTo(it, ::updateCurrSongData)
                }
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                super.onServiceDisconnected(name)
                viewModel.mediaPlayerService.clear()
            }
        }

        //connect to MediaPlayerService
        val intent = Intent(requireContext(), MediaPlayerService::class.java)
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun updateCurrSongData(songId: Long) {
        val songModel = viewModel.getSongById(songId) ?: return
        binding.songTitle.text = songModel.title
        binding.songAuthor.text = songModel.artist
        viewModel.loadSongItem(songModel.albumId, binding.songThumbnail)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        serviceConnection.unsubscribeToAllEvents()
        requireContext().unbindService(serviceConnection)
    }
}