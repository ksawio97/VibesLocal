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
import androidx.fragment.app.FragmentContainerView
import com.example.vibeslocal.R
import com.example.vibeslocal.activities.PlaybackDetailsActivity
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

        val currentSongItem = view.findViewById<FragmentContainerView>(R.id.currSongItem)
        currentSongItem.setOnClickListener {
            val intent = Intent(requireContext(), PlaybackDetailsActivity::class.java)
            startActivity(intent)
        }

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

        val toggleShowPlaybackControl : (Boolean) -> Unit = { isQueuePlaying ->
            //if it's not right
            if (isQueuePlaying != playbackControl.isVisible)
                playbackControl.visibility = if (isQueuePlaying) View.VISIBLE else View.GONE
        }

        //handle connection
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as MediaPlayerService.MediaPlayerBinder

                val mediaService = binder.getService()

                toggleShowPlaybackControl(mediaService.isQueuePlaying())
                togglePauseButtonIcon(mediaService.isPlaying())

                viewModel.mediaPlayerService = mediaService
                viewModel.subscribeToMediaPlayerEvent(MediaPlayerService.Events.PauseChangedEvent, togglePauseButtonIcon)
                viewModel.subscribeToMediaPlayerEvent(MediaPlayerService.Events.IsQueuePlayingChangedEvent, toggleShowPlaybackControl)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                viewModel.mediaPlayerService = null

                viewModel.unsubscribeToMediaPlayerEvent(MediaPlayerService.Events.PauseChangedEvent, togglePauseButtonIcon)
                viewModel.unsubscribeToMediaPlayerEvent(MediaPlayerService.Events.IsQueuePlayingChangedEvent, toggleShowPlaybackControl)
            }
        }

        //connect to MediaPlayerService
        val intent = Intent(requireContext(), MediaPlayerService::class.java)
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }
}