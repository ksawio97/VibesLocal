package com.example.vibeslocal.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.example.vibeslocal.R
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.viewmodels.PlaybackSongActionsViewModel
import org.koin.android.ext.android.inject

class PlaybackSongActionsFragment : Fragment() {
    private val viewModel: PlaybackSongActionsViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_playback_song_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val previousButton = view.findViewById<ImageButton>(R.id.previous_song_button)
        previousButton.setOnClickListener {
            viewModel.playPrevious()
        }
        val nextButton = view.findViewById<ImageButton>(R.id.next_song_button)
        nextButton.setOnClickListener {
            viewModel.playNext()
        }
        val pauseButton = view.findViewById<ImageButton>(R.id.pause_song_button)
        pauseButton.setOnClickListener {
            viewModel.pause()
        }

        //actions that depend on serviceConnection
        val togglePauseButtonIcon : (Boolean) -> Unit = { isPlaying ->
            val icon = if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
            pauseButton.setImageResource(icon)
        }

        //handle connection
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as MediaPlayerService.MediaPlayerBinder

                val mediaPlayerService = binder.getService()
                viewModel.mediaPlayerService = mediaPlayerService

                togglePauseButtonIcon(mediaPlayerService.isPlaying())
                viewModel.subscribeToMediaPlayerEvent(MediaPlayerService.Events.PauseChangedEvent, togglePauseButtonIcon)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                viewModel.mediaPlayerService = null

                viewModel.unsubscribeToMediaPlayerEvent(MediaPlayerService.Events.PauseChangedEvent, togglePauseButtonIcon)
            }
        }

        //connect to MediaPlayerService
        val intent = Intent(requireContext(), MediaPlayerService::class.java)
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }
}