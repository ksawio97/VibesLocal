package com.example.vibeslocal.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.vibeslocal.R
import com.example.vibeslocal.databinding.FragmentGroupedSongsInfoBinding
import com.example.vibeslocal.viewmodels.GroupedSongsInfoViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class GroupedSongsInfoFragment : Fragment(R.layout.fragment_grouped_songs_info) {
    private val viewModel: GroupedSongsInfoViewModel by activityViewModel()
    private lateinit var binding: FragmentGroupedSongsInfoBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentGroupedSongsInfoBinding.bind(view)
        viewModel.getGroupingInfo()?.let {  groupingModel ->

            binding.groupingThumbnail.apply {
                val thumbnail = viewModel.getThumbnail(groupingModel.thumbnailId)
                setImageBitmap(thumbnail)
            }
            binding.groupingTitle.apply {
                text = groupingModel.title
            }

            binding.groupingDescription.apply {
                text = groupingModel.description
            }
        }
    }
}