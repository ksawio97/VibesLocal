package com.example.vibeslocal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.R
import com.example.vibeslocal.models.GroupingOptionModel
import com.example.vibeslocal.models.SongModel

class GroupingOptionsListAdapter(private var groupingOptionsList: List<GroupingOptionModel>)
    : RecyclerView.Adapter<GroupingOptionsListAdapter.GroupingOptionHolder>() {

    private lateinit var onClickListener : OnItemClickListener
    interface OnItemClickListener {
        fun onItemClick(songModel: GroupingOptionModel)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupingOptionHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_grouping_option, parent, false)
        return GroupingOptionHolder(itemView, onClickListener)
    }

    override fun getItemCount(): Int {
        return groupingOptionsList.size
    }

    override fun onBindViewHolder(holder: GroupingOptionHolder, position: Int) {
        val currentItem = groupingOptionsList[position]
        holder.groupingOptionModel = currentItem
        holder.groupingOptionTitle.text = currentItem.title
    }

    inner class GroupingOptionHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        lateinit var groupingOptionModel : GroupingOptionModel
        val groupingOptionTitle : TextView = itemView.findViewById(R.id.option_title)

        init {
            itemView.setOnClickListener {
                if (::groupingOptionModel.isInitialized)
                    listener.onItemClick(groupingOptionModel)
            }
        }
    }
}