package com.example.vibeslocal.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.R
import com.example.vibeslocal.databinding.SongItemBinding
import com.example.vibeslocal.models.SongModel

class MusicItemsListAdapter(private var songsList: MutableList<SongModel>, private val getThumbnail: (Long) -> Bitmap) :
    RecyclerView.Adapter<MusicItemsListAdapter.SongHolder>() {

        private lateinit var onClickListener : OnItemClickListener
        interface OnItemClickListener {
            fun onItemClick(songModel: SongModel?)
        }

        fun setOnItemClickListener(listener: OnItemClickListener) {
            onClickListener = listener
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.song_item, parent, false)
            return SongHolder(itemView, onClickListener)
        }

        override fun onBindViewHolder(holder: SongHolder, position: Int) {
            val currentItem = songsList[position]
            holder.songModel = currentItem
            holder.songTitle.text = currentItem.title
            holder.songAuthor.text = currentItem.artist
            val thumbnail = getThumbnail(currentItem.albumId)
            holder.songThumbnail.setImageBitmap(thumbnail)
        }

        override fun getItemCount(): Int {
            return songsList.size
        }

        fun setSongs(newSongs: Collection<SongModel>){
            songsList.clear()
            songsList.addAll(newSongs)
            //TODO better notifications on changes
            notifyDataSetChanged()
        }

        inner class SongHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
            private val binding = SongItemBinding.bind(itemView)
            lateinit var songModel: SongModel
            val songTitle : TextView = binding.songTitle
            val songAuthor : TextView = binding.songAuthor
            val songThumbnail : ImageView = binding.songThumbnail

            init {
                itemView.setOnClickListener {
                    if (::songModel.isInitialized)
                        listener.onItemClick(songModel)
                }
            }
        }
}