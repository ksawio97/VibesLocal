package com.example.vibeslocal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vibeslocal.R

class PlaybackDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playback_details)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}