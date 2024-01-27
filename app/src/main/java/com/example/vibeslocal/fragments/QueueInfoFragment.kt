package com.example.vibeslocal.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.vibeslocal.R
import com.example.vibeslocal.adapters.MusicItemsListAdapter
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.viewmodels.MusicItemsViewModel
import com.example.vibeslocal.viewmodels.QueueInfoViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class QueueInfoFragment: Fragment(R.layout.fragment_queue_info)  {
    private val viewModel : QueueInfoViewModel by viewModel()
    private val musicItemsViewModel: MusicItemsViewModel by activityViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        musicItemsViewModel.setOnItemClickListener(object : MusicItemsListAdapter.OnItemClickListener {
            //TODO implement queue item click
            override fun onItemClick(songModel: SongModel) {
                Log.i(TAG, "Queue item clicked")
            }
        })
        //load queue songs to screen
        viewLifecycleOwner.lifecycleScope.launch {
            val songs = viewModel.getQueueSongs()
            musicItemsViewModel.loadSongsToAdapter(songs)
        }
    }

    companion object {
        const val TAG = "QueueInfoFragment"
    }
}