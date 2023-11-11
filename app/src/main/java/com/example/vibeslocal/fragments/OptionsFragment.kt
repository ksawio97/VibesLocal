package com.example.vibeslocal.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.vibeslocal.R
import com.example.vibeslocal.activities.GroupedSongsActivity
import com.example.vibeslocal.adapters.setupScrollProgress
import com.example.vibeslocal.databinding.FragmentOptionsBinding
import com.example.vibeslocal.models.GroupingInfoModel
import com.example.vibeslocal.models.IGroupingDescriptionStrategy
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.viewmodels.OptionsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class OptionsFragment<T>
    (private val selector: (SongModel) -> T,
    private val groupingDescriptionStrategy: IGroupingDescriptionStrategy)
    : Fragment(R.layout.fragment_options) {
    private val viewModel: OptionsViewModel by viewModel()
    private lateinit var binding: FragmentOptionsBinding
    private lateinit var cleanupProgressBar: () -> Unit
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOptionsBinding.bind(view)


        binding.groupingOptions.layoutAnimation =
            LayoutAnimationController(AnimationUtils.loadAnimation(context, R.anim.alpha_1))

        viewModel.configureRecyclerView(binding.groupingOptions) { optionModel, songsToSend, getOptionThumbnail ->
            val intent = Intent(context, GroupedSongsActivity::class.java).apply {
                val bundle = Bundle().apply {
                    putParcelableArray(GroupedSongsActivity.retrievedSongs, songsToSend.toTypedArray())
                    putParcelable(GroupedSongsActivity.retrievedGroupingInfo,
                        GroupingInfoModel(
                            getOptionThumbnail,
                            optionModel.title,
                            groupingDescriptionStrategy.getDescription(optionModel, songsToSend)))
                }
                putExtras(bundle)
            }
            startActivity(intent)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadGroupedSongs(selector)
        }.invokeOnCompletion {
            //stop showing progress bar
            binding.progressBar.visibility = View.GONE

            viewModel.addOptions()
        }
        //setup scroll_progress
        cleanupProgressBar = binding.groupingOptions.setupScrollProgress(binding.scrollProgress)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        cleanupProgressBar()
    }
}