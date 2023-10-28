package com.example.vibeslocal.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.vibeslocal.adapters.GroupingPagerAdapter
import com.example.vibeslocal.databinding.ActivityMainBinding
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.viewmodels.CurrentPageViewModel
import com.example.vibeslocal.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel : MainViewModel by viewModel()
    private val currentPageViewModel : CurrentPageViewModel by viewModel()

    private lateinit var binding : ActivityMainBinding

    private lateinit var currentPageChanges: (Int) -> Unit
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
        val mediaPlayerService = Intent(this, MediaPlayerService::class.java)
        startService(mediaPlayerService)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //#region setting up changing currentItem in groupingPager based on tab
        binding.groupingPager.currentItem = currentPageViewModel.getCurrentItem()
        currentPageChanges = {
            if (binding.groupingPager.currentItem != it)
                binding.groupingPager.currentItem = it
        }
        currentPageViewModel.subscribe(currentPageChanges)
        //#endregion

        //adding action to inform changing page in groupingPager
        binding.groupingPager.registerOnPageChangeCallback(currentPageViewModel.onPageChangeCallback)

        val selectors = listOf(SongModel::artist, SongModel::albumId, SongModel::artist, SongModel::genre)
        //attaching adapter
        binding.groupingPager.adapter = GroupingPagerAdapter(supportFragmentManager, lifecycle, selectors)
    }

    override fun onDestroy() {
        super.onDestroy()

        currentPageViewModel.unsubscribe(currentPageChanges)
    }
}