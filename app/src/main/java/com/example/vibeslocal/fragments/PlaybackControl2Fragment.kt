package com.example.vibeslocal.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.fragment.app.Fragment
import com.example.vibeslocal.R
import com.example.vibeslocal.databinding.FragmentPlaybackControl2Binding
import com.example.vibeslocal.events.ServiceConnectionWithEventManager
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.viewmodels.PlaybackControl2ViewModel
import org.koin.android.ext.android.inject
import java.lang.ref.WeakReference

class PlaybackControl2Fragment : Fragment(R.layout.fragment_playback_control_2) {
    private val viewModel: PlaybackControl2ViewModel by inject()
    private lateinit var binding: FragmentPlaybackControl2Binding
    private lateinit var serviceConnection: ServiceConnectionWithEventManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlaybackControl2Binding.bind(view)

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
                //set to true because when song gets changed MediaPlayer starts playing
                eventManager.subscribeTo(mediaPlayerService.currentSongChangedEvent) { _ ->
                    togglePauseButtonIcon(true)
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

    override fun onDestroyView() {
        super.onDestroyView()

        serviceConnection.unsubscribeToAllEvents()
        requireContext().unbindService(serviceConnection)
    }
}