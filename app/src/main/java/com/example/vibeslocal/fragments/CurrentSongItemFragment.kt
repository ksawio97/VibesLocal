package com.example.vibeslocal.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.vibeslocal.R
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.viewmodels.CurrentSongItemViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrentSongItemFragment : Fragment() {
    private val viewModel : CurrentSongItemViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.song_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        val onCurrentSongChangedEvent: (SongModel?) -> Unit = { model ->
            if (model != null)
            {
                val songTitle : TextView = view.findViewById(R.id.song_title)
                val songAuthor : TextView = view.findViewById(R.id.song_author)
                val songThumbnail : ImageView = view.findViewById(R.id.song_thumbnail)

                songTitle.text = model.title
                songAuthor.text = model.artist

                viewModel.loadSongItem(model.albumId, songThumbnail)
            }
        }
        //handle connection
        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as MediaPlayerService.MediaPlayerBinder
                viewModel.mediaPlayerService = binder.getService()

                viewModel.subscribeToMediaPlayerEvent(MediaPlayerService.Events.CurrentSongChangedEvent, onCurrentSongChangedEvent)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                viewModel.mediaPlayerService = null

                viewModel.unsubscribeToMediaPlayerEvent(MediaPlayerService.Events.CurrentSongChangedEvent, onCurrentSongChangedEvent)
            }
        }

        //connect to MediaPlayerService
        val intent = Intent(requireContext(), MediaPlayerService::class.java)
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }
}