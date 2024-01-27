package com.example.vibeslocal.activities

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.example.vibeslocal.databinding.ActivityGroupedSongsBinding
import com.example.vibeslocal.models.GroupingInfoModel
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.viewmodels.GroupedSongsInfoViewModel
import com.example.vibeslocal.viewmodels.GroupedSongsViewModel
import com.example.vibeslocal.viewmodels.MusicItemsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.ref.WeakReference

class GroupedSongsActivity : AppCompatActivity() {
    private val viewModel: GroupedSongsViewModel by viewModel()
    private val musicItemsViewModel: MusicItemsViewModel by viewModel()
    private val groupedSongsInfoViewModel: GroupedSongsInfoViewModel by viewModel()
    private lateinit var binding: ActivityGroupedSongsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //send data to adapter
        musicItemsViewModel.loadSongsToAdapter(getSongArray().toList())
        musicItemsViewModel.setOnItemClickListener(viewModel.getMusicItemClickHandler(WeakReference(musicItemsViewModel)))

        //send data to groupedSongsInfoViewModel
        val groupingInfo = getGroupingInfo()
        if (groupingInfo != null)
            groupedSongsInfoViewModel.setGroupingInfo(groupingInfo)

        binding = ActivityGroupedSongsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //connect to MediaPlayerService
        val intent = Intent(this, MediaPlayerService::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private fun getSongArray(): Array<SongModel>{
        val bundle = intent.extras
        return bundle?.getParcelableArray(retrievedSongs, SongModel::class.java) ?: emptyArray()
    }

    private fun getGroupingInfo(): GroupingInfoModel? {
        val bundle = intent.extras
        return bundle?.getParcelable(retrievedGroupingInfo, GroupingInfoModel::class.java)
    }

    override fun onDestroy() {
        super.onDestroy()

        unbindService(serviceConnection)
    }

    //handle connection
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as MediaPlayerService.MediaPlayerBinder
            viewModel.mediaPlayerService = WeakReference(binder.getService())
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            viewModel.mediaPlayerService.clear()
        }
    }

    companion object {
        const val retrievedSongs: String = "retrieved_songs"
        const val retrievedGroupingInfo: String = "retrieved_grouping_info"
    }
}