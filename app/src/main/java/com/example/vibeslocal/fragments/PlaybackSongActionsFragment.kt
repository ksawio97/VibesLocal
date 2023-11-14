package com.example.vibeslocal.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.fragment.app.Fragment
import com.example.vibeslocal.R
import com.example.vibeslocal.databinding.FragmentPlaybackSongDetailsBinding
import com.example.vibeslocal.events.ServiceConnectionWithEventManager
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.viewmodels.PlaybackSongActionsViewModel
import org.koin.android.ext.android.inject
import java.lang.ref.WeakReference

class PlaybackSongActionsFragment : Fragment(R.layout.fragment_playback_song_details) {
    private val viewModel: PlaybackSongActionsViewModel by inject()
    private lateinit var binding: FragmentPlaybackSongDetailsBinding
    private lateinit var serviceConnection: ServiceConnectionWithEventManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlaybackSongDetailsBinding.bind(view)

        //region setting up basic onClick actions
        binding.previousSongButton.setOnClickListener {
            viewModel.playPrevious()
        }
        binding.nextSongButton.setOnClickListener {
            viewModel.playNext()
        }
        binding.pauseSongButton.setOnClickListener {
            viewModel.pause()
        }
        //endregion

        //actions that depend on serviceConnection
        val togglePauseButtonIcon : (Boolean) -> Unit = { isPlaying ->
            val icon = if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
            binding.pauseSongButton.setImageResource(icon)
        }

        //handle connection
        serviceConnection = object : ServiceConnectionWithEventManager() {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as MediaPlayerService.MediaPlayerBinder

                val mediaPlayerService = binder.getService()
                viewModel.mediaPlayerService = WeakReference(mediaPlayerService)

                togglePauseButtonIcon(mediaPlayerService.isPlaying())
                eventManager.subscribeTo(mediaPlayerService.pauseChangedEvent, togglePauseButtonIcon)
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

    override fun onDestroyView() {
        super.onDestroyView()

        serviceConnection.unsubscribeToAllEvents()
        requireContext().unbindService(serviceConnection)
    }
}