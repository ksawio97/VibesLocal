package com.example.vibeslocal.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.vibeslocal.R
import com.example.vibeslocal.fragments.PlaybackSongDetailsFragment

class PlaybackDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playback_details)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PlaybackSongDetailsFragment.newInstance())
                .commitNow()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}