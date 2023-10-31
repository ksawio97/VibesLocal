package com.example.vibeslocal.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import androidx.fragment.app.Fragment
import com.example.vibeslocal.R
import com.example.vibeslocal.databinding.FragmentMusicItemsBinding
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.viewmodels.MusicItemsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MusicItemsFragment : Fragment(R.layout.fragment_music_items) {
    private val viewModel : MusicItemsViewModel by activityViewModel()
    private lateinit var binding: FragmentMusicItemsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMusicItemsBinding.bind(view)

        viewModel.configureRecyclerView(binding.musicItemsList)

        //connect to MediaPlayerService
        val intent = Intent(requireContext(), MediaPlayerService::class.java)
        requireContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    //handle connection
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MediaPlayerService.MediaPlayerBinder
            viewModel.mediaPlayerService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.mediaPlayerService = null
        }
    }
}