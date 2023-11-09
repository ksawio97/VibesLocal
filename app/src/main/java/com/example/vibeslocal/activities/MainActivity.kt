package com.example.vibeslocal.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.vibeslocal.adapters.GroupingPagerAdapter
import com.example.vibeslocal.databinding.ActivityMainBinding
import com.example.vibeslocal.models.AlbumsDescriptionStrategy
import com.example.vibeslocal.models.ArtistsDescriptionStrategy
import com.example.vibeslocal.models.GenresDescriptionStrategy
import com.example.vibeslocal.models.PlaylistDescriptionStrategy
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
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupRequests()
        askUserForPermissions()
        startLoadingAnimation()

        val mediaPlayerService = Intent(this, MediaPlayerService::class.java)
        startService(mediaPlayerService)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        changeUI()
    }

    override fun onDestroy() {
        super.onDestroy()

        currentPageViewModel.unsubscribe(currentPageChanges)
    }

    private fun changeUI() {
        if (!::binding.isInitialized)
            return

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
        binding.groupingPager.adapter = GroupingPagerAdapter(supportFragmentManager, lifecycle, selectors) {position ->
            when (position) {
                0 -> PlaylistDescriptionStrategy()
                1 -> AlbumsDescriptionStrategy()
                2 -> ArtistsDescriptionStrategy()
                3 -> GenresDescriptionStrategy()
                else -> {throw StackOverflowError("There shouldn't be more than 4 options!")}
            }
        }
    }
    private fun setupRequests() {
        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    viewModel.loadSongsToSongsRepository()
                    Log.i(TAG, "Permission $requestedPermission granted")
                } else {
                    viewModel.stopLoading()
                    showExplanationDialog()
                }
            }
        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (ContextCompat.checkSelfPermission(this, requestedPermission) == PackageManager.PERMISSION_GRANTED) {
                viewModel.restartLoading()
                startLoadingAnimation()

                Log.i(TAG, "restarted loading")
            }
        }
    }

    private fun startLoadingAnimation() {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }
    }

    private fun askUserForPermissions() {
        requestPermissionLauncher.launch(requestedPermission)
    }


    private fun showExplanationDialog() {
        AlertDialog.Builder(this)
            .setMessage("This app won't work without the necessary permissions. Will you enable them?")
            .setTitle("Permissions are necessary")
            .setNegativeButton("No, Thanks") { _, _ -> Log.i(TAG, "Permission $requestedPermission denied") }
            .setPositiveButton("Ok") { _, _ -> openSettings() }
            .create()
            .show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startForResult.launch(intent)
        //startActivity(intent)
    }

    companion object {
        const val TAG: String = "MainActivity"
        const val requestedPermission = android.Manifest.permission.READ_MEDIA_AUDIO
    }
}