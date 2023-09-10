package com.example.vibeslocal.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.adapters.MusicItemsListAdapter
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.repositories.SongsRepository
import com.example.vibeslocal.services.MediaPlayerService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicItemsViewModel(private val songsRepository: SongsRepository) : ViewModel() {
    private lateinit var musicItemsListAdapter: MusicItemsListAdapter
    //TODO delete it when SongsRepository will be observable
    private var viewLoaded = false

    var mediaPlayerService: MediaPlayerService? = null
    var isBound = false

    fun ConfigureRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        musicItemsListAdapter = MusicItemsListAdapter(songsRepository.getAll().toMutableList())
        recyclerView.adapter = musicItemsListAdapter


        musicItemsListAdapter.setOnItemClickListener(object: MusicItemsListAdapter.OnItemClickListener {
            override fun onItemClick(songModel: SongModel?) {
                if(songModel == null)
                    return
                Log.i("Debug", "Playing song ${songModel.title}")
                mediaPlayerService?.play(songModel.uri)
            }
        })

    }
    fun loadDataToRepository() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                songsRepository.loadData()
            }
        }.invokeOnCompletion {
            //TODO delete it when SongsRepository will be observable
            if (viewLoaded)
                musicItemsListAdapter.setSongs(songsRepository.getAll())
        }
    }

    fun loadDataToAdapter() {
        //TODO delete it when SongsRepository will be observable and add observe method
        viewLoaded = true

        musicItemsListAdapter.setSongs(songsRepository.getAll())
    }
}