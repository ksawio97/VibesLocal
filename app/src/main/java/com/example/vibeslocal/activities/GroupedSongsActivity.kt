package com.example.vibeslocal.activities

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.vibeslocal.databinding.ActivityGroupedSongsBinding
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.viewmodels.MusicItemsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GroupedSongsActivity : AppCompatActivity() {
    private val musicItemsViewModel : MusicItemsViewModel by viewModel()

    private lateinit var binding : ActivityGroupedSongsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val songs = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) getSongArray().toList() else emptyList()
        //send data to adapter
        musicItemsViewModel.loadDataToAdapter(songs)

        binding = ActivityGroupedSongsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getSongArray() : Array<SongModel>{
        val bundle = intent.extras
        return bundle?.getParcelableArray(retrievedSongs, SongModel::class.java) ?: emptyArray()
    }

    companion object {
        const val retrievedSongs: String = "retrieved_songs"
    }
}