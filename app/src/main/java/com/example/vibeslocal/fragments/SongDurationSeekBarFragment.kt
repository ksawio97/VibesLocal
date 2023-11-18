package com.example.vibeslocal.fragments

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.example.vibeslocal.R
import com.example.vibeslocal.databinding.FragmentSongDurationSeekBarBinding
import com.example.vibeslocal.events.ServiceConnectionWithEventManager
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.viewmodels.SongDurationSeekBarViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ref.WeakReference

class SongDurationSeekBarFragment : Fragment(R.layout.fragment_song_duration_seek_bar) {
    private val viewModel: SongDurationSeekBarViewModel by viewModel()
    private lateinit var binding: FragmentSongDurationSeekBarBinding

    private lateinit var serviceConnection: ServiceConnectionWithEventManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSongDurationSeekBarBinding.bind(view)

        binding.seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    viewModel.setPlaybackProgress(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                viewModel.suspenseUpdating = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                viewModel.suspenseUpdating = false
            }
        })

        serviceConnection = object: ServiceConnectionWithEventManager() {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as MediaPlayerService.MediaPlayerBinder
                val mediaPlayerService = binder.getService()

                viewModel.mediaPlayerService = WeakReference(mediaPlayerService)

                //set max progress
                binding.seekBar.max = viewModel.getPlaybackDuration()
                mediaPlayerService.currentSongChangedEvent.let {
                    eventManager.subscribeTo(it) {
                        binding.seekBar.max = viewModel.getPlaybackDuration()
                    }
                }
                viewModel.startUpdatingProgressBar(binding.seekBar::setProgress)
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