package com.example.vibeslocal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.R
import com.example.vibeslocal.models.SongModel

class SongListAdapter(private val songsList: Array<SongModel>) :
    RecyclerView.Adapter<SongListAdapter.SongHolder>() {

    private lateinit var onClickListener : onItemClickListener
    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        onClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)
        return SongHolder(itemView, onClickListener)
    }

    override fun onBindViewHolder(holder: SongHolder, position: Int) {
        val currentItem = songsList[position]
        holder.songTitle.text = currentItem.title
        holder.songAuthor.text = currentItem.artist
        if (currentItem.thumbnail == null)
            holder.songThumbnail.setImageResource(R.drawable.unknown)
        else
            holder.songThumbnail.setImageBitmap(currentItem.thumbnail)
    }

    override fun getItemCount(): Int {
        return songsList.size
    }

    inner class SongHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val songTitle : TextView = itemView.findViewById(R.id.song_title)
        val songAuthor : TextView = itemView.findViewById(R.id.song_author)
        val songThumbnail : ImageView = itemView.findViewById(R.id.song_thumbnail)

        init {
           itemView.setOnClickListener {
               listener.onItemClick(adapterPosition)
           }
        }
    }
}