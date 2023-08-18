package com.example.vibeslocal

import com.example.vibeslocal.R
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.vibeslocal.adapters.SongListAdapter
import com.example.vibeslocal.viewmodels.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


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
            lifecycleScope.launch {
                val songs = withContext(Dispatchers.IO) {
                    viewModel.loadData(contentResolver)
                }
                songs?.let {
                    var adapter = SongListAdapter(it)
                    songListRecyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object: SongListAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            Toast.makeText(this@MainActivity, "You clicked song nr $position!", Toast.LENGTH_LONG).show()
                            Log.i("Debug", "Song nr $position have been clicked!")
                        }
                    })
                }
            }

        }
    }
}