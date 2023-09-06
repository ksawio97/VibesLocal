package com.example.vibeslocal.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.R
import com.example.vibeslocal.adapters.MusicItemsListAdapter
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.viewmodels.MusicItemsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class MusicItemsFragment : Fragment() {
    private val viewModel : MusicItemsViewModel by viewModel()

    private lateinit var musicItemsListAdapter: MusicItemsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_music_items, container, false)
        val musicItemsListRecyclerView : RecyclerView = view.findViewById(R.id.music_items_list)
        musicItemsListRecyclerView.setHasFixedSize(true)
        musicItemsListAdapter = MusicItemsListAdapter(emptyArray())
        musicItemsListRecyclerView.adapter = musicItemsListAdapter

        musicItemsListAdapter.setOnItemClickListener(object: MusicItemsListAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                Toast.makeText(
                    activity,
                    "You clicked song nr $position!",
                    Toast.LENGTH_LONG
                ).show()
                Log.i("Debug", "Song nr $position have been clicked!")
            }
        })

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val songs = withContext(Dispatchers.IO) {
                if (activity?.contentResolver != null)
                    viewModel.loadData(activity?.contentResolver!!)
                else
                    null
            }
            songs?.let {
                musicItemsListAdapter.updateData(it)
            }
        }
    }
}