package com.example.vibeslocal.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.vibeslocal.R
import com.example.vibeslocal.databinding.FragmentOptionsBinding
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.viewmodels.OptionsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class OptionsFragment<T>(private val selector: (SongModel) -> T) : Fragment(R.layout.fragment_options) {
    private val viewModel: OptionsViewModel by viewModel()
    private lateinit var binding: FragmentOptionsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentOptionsBinding.bind(view)
        viewModel.configureRecyclerView(binding.groupingOptions)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadGroupedSongs(selector)
        }.invokeOnCompletion {
            //stop showing progress bar
            binding.progressBar.visibility = View.GONE

            viewModel.addOptions()
        }
    }
}