package com.example.vibeslocal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.R
import com.example.vibeslocal.models.GroupingOptionModel

class GroupingOptionsListAdapter(private var groupingOptionsList: List<GroupingOptionModel>)
    : RecyclerView.Adapter<GroupingOptionsListAdapter.GroupingOptionHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupingOptionHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_grouping_option, parent, false)
        return GroupingOptionHolder(itemView)
    }

    override fun getItemCount(): Int {
        return groupingOptionsList.size
    }

    override fun onBindViewHolder(holder: GroupingOptionHolder, position: Int) {
        val currentItem = groupingOptionsList[position]
        holder.groupingOptionTitle.text = currentItem.title
    }

    inner class GroupingOptionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupingOptionTitle : TextView = itemView.findViewById(R.id.option_title)
    }
}