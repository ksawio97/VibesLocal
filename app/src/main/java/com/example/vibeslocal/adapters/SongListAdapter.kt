package com.example.vibeslocal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.R
import com.example.vibeslocal.models.SongModel

class SongListAdapter(private val songsList: Array<SongModel>) :
    RecyclerView.Adapter<SongListAdapter.SongHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)
        return SongHolder(itemView)
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        val currentItem = songsList[position]
        holder.songTitle.text = currentItem.title
        holder.songAuthor.text = currentItem.artist
    }

    override fun getItemCount(): Int {
        return songsList.size
    }

    inner class SongHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val songTitle : TextView = itemView.findViewById(R.id.song_title)
        val songAuthor : TextView = itemView.findViewById(R.id.song_author)
    }
}