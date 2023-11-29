package com.example.vibeslocal.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.fragment.app.Fragment
import com.example.vibeslocal.R
import com.example.vibeslocal.databinding.FragmentCurrentSongInfoBinding
import com.example.vibeslocal.events.ServiceConnectionWithEventManager
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.viewmodels.CurrentSongInfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrentSongInfoFragment : Fragment(R.layout.fragment_current_song_info) {
    private val viewModel: CurrentSongInfoViewModel by viewModel()
    private lateinit var binding: FragmentCurrentSongInfoBinding
    private lateinit var serviceConnection: ServiceConnectionWithEventManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCurrentSongInfoBinding.bind(view)

        serviceConnection = object : ServiceConnectionWithEventManager() {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as MediaPlayerService.MediaPlayerBinder
                val mediaPlayerService = binder.getService()

                setSongInfo(viewModel.getCurrentSongId())
                eventManager.subscribeTo(mediaPlayerService.currentSongChangedEvent, ::setSongInfo)
            }
        }

        //connect to MediaPlayerService
        val intent = Intent(requireContext(), MediaPlayerService::class.java)
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun setSongInfo(songId: Long) {
        viewModel.getSongInfo(songId)?.also { songModel ->
            binding.titleText.text = songModel.title
            binding.descriptionText.text = songModel.artist
            val thumbnail = viewModel.getSongThumbnail(songModel.albumId)
            binding.currentSongThumbnail.setImageBitmap(thumbnail)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        serviceConnection.unsubscribeToAllEvents()
        requireContext().unbindService(serviceConnection)
    }
}