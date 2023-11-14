package com.example.vibeslocal.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.vibeslocal.R
import com.example.vibeslocal.activities.PlaybackDetailsActivity
import com.example.vibeslocal.databinding.FragmentPlaybackControlBinding
import com.example.vibeslocal.events.ServiceConnectionWithEventManager
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.viewmodels.PlaybackControlViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ref.WeakReference

class PlaybackControlFragment : Fragment(R.layout.fragment_playback_control) {
    private val viewModel: PlaybackControlViewModel by viewModel()
    private lateinit var binding: FragmentPlaybackControlBinding
    private lateinit var serviceConnection: ServiceConnectionWithEventManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPlaybackControlBinding.bind(view)

        binding.playbackControl.visibility = View.GONE

        //region setting up basic onClick actions
        binding.currSongItem.setOnClickListener {
            val intent = Intent(requireContext(), PlaybackDetailsActivity::class.java)
            startActivity(intent)
        }

        binding.pauseButton.setOnClickListener {
            viewModel.pauseSong()
        }
        //endregion

        //actions that depend on serviceConnection
        val togglePauseButtonIcon : (Boolean) -> Unit = { isPlaying ->
            val icon = if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
            binding.pauseButton.setImageResource(icon)
        }

        val toggleShowPlaybackControl : (Boolean) -> Unit = { isQueuePlaying ->
            //if it's not right
            if (isQueuePlaying != binding.playbackControl.isVisible)
                binding.playbackControl.visibility = if (isQueuePlaying) View.VISIBLE else View.GONE
        }

        //handle connection
        serviceConnection = object : ServiceConnectionWithEventManager() {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as MediaPlayerService.MediaPlayerBinder
                val mediaPlayerService = binder.getService()

                toggleShowPlaybackControl(mediaPlayerService.isQueuePlaying())
                togglePauseButtonIcon(mediaPlayerService.isPlaying())

                viewModel.mediaPlayerService = WeakReference(mediaPlayerService)
                //set max progress
                binding.playbackProgress.max = viewModel.getPlaybackDuration()
                mediaPlayerService.currentSongChangedEvent.let {
                    eventManager.subscribeTo(it) {
                        binding.playbackProgress.max = viewModel.getPlaybackDuration()
                    }
                }
                viewModel.startUpdatingProgressBar(binding.playbackProgress::setProgress)

                mediaPlayerService.pauseChangedEvent.let {
                    eventManager.subscribeTo(it, togglePauseButtonIcon)
                }

                mediaPlayerService.isQueuePlayingEvent.let {
                    eventManager.subscribeTo(it, toggleShowPlaybackControl)
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