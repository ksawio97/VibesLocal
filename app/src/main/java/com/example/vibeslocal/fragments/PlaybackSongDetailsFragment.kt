package com.example.vibeslocal.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.vibeslocal.R
import com.example.vibeslocal.viewmodels.PlaybackDetailsViewModel

class PlaybackSongDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = PlaybackSongDetailsFragment()
    }

    private lateinit var viewModel: PlaybackDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlaybackDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_playback_song_details, container, false)
    }

}