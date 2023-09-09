package com.example.vibeslocal.viewmodels

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.adapters.MusicItemsListAdapter
import com.example.vibeslocal.repositories.SongsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicItemsViewModel(private val songsRepository: SongsRepository) : ViewModel() {
    private lateinit var musicItemsListAdapter: MusicItemsListAdapter
    //TODO delete it when SongsRepository will be observable
    private var viewLoaded = false

    fun ConfigureRecyclerView(recyclerView: RecyclerView, clickAction: (text: String) -> Toast) {
        recyclerView.setHasFixedSize(true)
        musicItemsListAdapter = MusicItemsListAdapter(songsRepository.getAll().toMutableList())
        recyclerView.adapter = musicItemsListAdapter


        musicItemsListAdapter.setOnItemClickListener(object: MusicItemsListAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                clickAction("You clicked song nr $position!").show()
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