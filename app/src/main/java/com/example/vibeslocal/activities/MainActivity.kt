package com.example.vibeslocal.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vibeslocal.R
import com.example.vibeslocal.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val mainViewModel : MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}