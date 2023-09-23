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
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.example.vibeslocal.R
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.viewmodels.PlaybackControlViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaybackControlFragment : Fragment() {
    private val viewModel: PlaybackControlViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_playback_control, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playbackControl = view.findViewById<LinearLayout>(R.id.playbackControl)
        playbackControl.visibility = View.GONE

        //region setting up basic onClick actions
        val pauseButton = view.findViewById<ImageButton>(R.id.pauseButton)

        pauseButton.setOnClickListener {
            viewModel.pauseSong()
        }
        //endregion

        //actions that depend on serviceConnection
        val togglePauseButtonIcon : (Boolean) -> Unit = { isPlaying ->
            val icon = if (isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
            pauseButton.setImageResource(icon)
        }

        val toggleShowPlaybackControl : (Boolean) -> Unit = { queueNotEmpty ->
            //if it's not right
            if (queueNotEmpty != playbackControl.isVisible)
                playbackControl.visibility = if (queueNotEmpty) View.VISIBLE else View.GONE
        }

        //handle connection
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as MediaPlayerService.MediaPlayerBinder
                viewModel.mediaPlayerService = binder.getService()

                viewModel.subscribeToMediaPlayerEvent(MediaPlayerService.Events.pauseChangedEvent, togglePauseButtonIcon)
                viewModel.subscribeToMediaPlayerEvent(MediaPlayerService.Events.isQueuePlayingChangedEvent, toggleShowPlaybackControl)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                viewModel.mediaPlayerService = null

                viewModel.unsubscribeToMediaPlayerEvent(MediaPlayerService.Events.pauseChangedEvent, togglePauseButtonIcon)
                viewModel.unsubscribeToMediaPlayerEvent(MediaPlayerService.Events.isQueuePlayingChangedEvent, toggleShowPlaybackControl)
            }
        }

        //connect to MediaPlayerService
        val intent = Intent(requireContext(), MediaPlayerService::class.java)
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }
}