package com.example.vibeslocal.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.vibeslocal.R
import com.example.vibeslocal.databinding.FragmentQueueInfoBinding
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.viewmodels.MusicItemsViewModel
import com.example.vibeslocal.viewmodels.QueueInfoViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class QueueInfoFragment: Fragment(R.layout.fragment_queue_info)  {
    private val viewModel : QueueInfoViewModel by viewModel()
    private val musicItemsViewModel: MusicItemsViewModel by activityViewModel()
    private lateinit var binding: FragmentQueueInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //load queue songs to screen
        val songs = viewModel.getQueueSongs()
        //TODO something here doesn't work, music items do not show up, maybe do it in activity?
        musicItemsViewModel.loadDataToAdapter(songs)
    }
}