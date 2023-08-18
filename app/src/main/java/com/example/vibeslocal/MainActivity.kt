package com.example.vibeslocal

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.adapters.SongListAdapter
import com.example.vibeslocal.viewmodels.MainViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var songListRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        songListRecyclerView = findViewById(R.id.song_list)
        songListRecyclerView.setHasFixedSize(true)

        val btn1 = findViewById<Button>(R.id.btn1)
        btn1.setOnClickListener {
            val songs = viewModel.loadData(contentResolver) ?: return@setOnClickListener
            songListRecyclerView.adapter = SongListAdapter(songs)
        }
    }
}