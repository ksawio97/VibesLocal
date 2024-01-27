package com.example.vibeslocal.viewmodels

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.adapters.MusicItemsListAdapter
import com.example.vibeslocal.models.SongModel

class MusicItemsViewModel : ViewModel() {
    private val musicItemsListAdapter: MusicItemsListAdapter = MusicItemsListAdapter(mutableListOf())

    /** Attaches Adapter to recycler view
     * @param recyclerView recycler view that adapter is being attached to
     */
    fun configureRecyclerView(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = musicItemsListAdapter
    }

    /**
     * @param listener defined on music item click behavior
     */
    fun setOnItemClickListener(listener: MusicItemsListAdapter.OnItemClickListener) {
        musicItemsListAdapter.setOnItemClickListener(listener)
    }

    /**
     * @param songs sets songs inside adapter that are being shown
     */
    fun loadSongsToAdapter(songs: Collection<SongModel>) {
        musicItemsListAdapter.setSongs(songs)
    }

    /**
     * @return songs in music items adapter (copy)
     */
    fun getSongsFromAdapter() : Collection<SongModel> {
        return musicItemsListAdapter.getSongs()
    }
}