package com.example.vibeslocal.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vibeslocal.databinding.ActivityGroupedSongsBinding

class GroupedSongsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityGroupedSongsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGroupedSongsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}