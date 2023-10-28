package com.example.vibeslocal.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.R
import com.example.vibeslocal.databinding.FragmentGroupingOptionBinding
import com.example.vibeslocal.models.OptionModel
//TODO add some animations
class OptionsListAdapter
    : RecyclerView.Adapter<OptionsListAdapter.OptionHolder>() {
    private val optionsList = mutableListOf<OptionModel>()

    private lateinit var onClickListener : OnItemClickListener
    interface OnItemClickListener {
        fun onItemClick(optionModel: OptionModel)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.fragment_grouping_option, parent, false)
        return OptionHolder(itemView, onClickListener)
    }

    override fun getItemCount(): Int {
        return optionsList.size
    }

    override fun onBindViewHolder(holder: OptionHolder, position: Int) {
        val currentItem = optionsList[position]
        holder.optionModel = currentItem
        holder.optionTitle.text = currentItem.title
    }

    inner class OptionHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        private val binding = FragmentGroupingOptionBinding.bind(itemView)
        lateinit var optionModel : OptionModel
        val optionTitle : TextView = binding.optionTitle

        init {
            itemView.setOnClickListener {
                if (::optionModel.isInitialized)
                    listener.onItemClick(optionModel)
            }
        }
    }

    fun addItems(items: Collection<OptionModel>) {
        val lastPosition = optionsList.size
        optionsList.addAll(lastPosition, items)
        notifyItemRangeInserted(lastPosition, items.size)
    }
}