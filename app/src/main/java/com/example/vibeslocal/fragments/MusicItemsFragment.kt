package com.example.vibeslocal.fragments

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import androidx.fragment.app.Fragment
import com.example.vibeslocal.R
import com.example.vibeslocal.adapters.setupScrollProgress
import com.example.vibeslocal.databinding.FragmentMusicItemsBinding
import com.example.vibeslocal.utils.SpacingItemDecoration
import com.example.vibeslocal.viewmodels.MusicItemsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class MusicItemsFragment : Fragment(R.layout.fragment_music_items) {
    private val viewModel : MusicItemsViewModel by activityViewModel()
    private lateinit var binding: FragmentMusicItemsBinding
    private lateinit var cleanupProgressBar: () -> Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMusicItemsBinding.bind(view)
        binding.musicItemsList.layoutAnimation =
            LayoutAnimationController(AnimationUtils.loadAnimation(context, R.anim.translate))

        viewModel.configureRecyclerView(binding.musicItemsList)

        context?.let {
            binding.musicItemsList.addItemDecoration(SpacingItemDecoration(it, SpacingItemDecoration.Spacing(10, 0, 10, 0)))
        }

        //setup scroll_progress
        cleanupProgressBar = binding.musicItemsList.setupScrollProgress(binding.scrollProgress)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        cleanupProgressBar()
    }
}