package com.example.vibeslocal.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.vibeslocal.R
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel : MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        val mediaPlayerService = Intent(this, MediaPlayerService::class.java)
        startService(mediaPlayerService)

        setContentView(R.layout.activity_main)
    }
}