package com.example.vibeslocal.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vibeslocal.R

class PlaybackDetailsActivity : AppCompatActivity(R.layout.activity_playback_details) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}