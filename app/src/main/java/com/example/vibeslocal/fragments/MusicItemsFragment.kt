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
import androidx.fragment.app.Fragment
import com.example.vibeslocal.R
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.viewmodels.MusicItemsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MusicItemsFragment : Fragment() {
    private val viewModel : MusicItemsViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_music_items, container, false)
        viewModel.ConfigureRecyclerView(view.findViewById(R.id.music_items_list))

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.loadDataToRepository()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadDataToAdapter()

        //connect to MediaPlayerService
        val intent = Intent(requireContext(), MediaPlayerService::class.java)
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    //handle connection
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MediaPlayerService.MediaPlayerBinder
            viewModel.mediaPlayerService = binder.getService()
            viewModel.isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.mediaPlayerService = null
            viewModel.isBound = false
        }
    }
}