package com.example.vibeslocal.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.vibeslocal.databinding.ActivityGroupedSongsBinding
import com.example.vibeslocal.models.GroupingInfoModel
import com.example.vibeslocal.models.SongModel
import com.example.vibeslocal.viewmodels.GroupedSongsInfoViewModel
import com.example.vibeslocal.viewmodels.MusicItemsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class GroupedSongsActivity : AppCompatActivity() {
    private val musicItemsViewModel: MusicItemsViewModel by viewModel()
    private val groupedSongsInfoViewModel: GroupedSongsInfoViewModel by viewModel()
    private lateinit var binding: ActivityGroupedSongsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //send data to adapter
        musicItemsViewModel.loadDataToAdapter(getSongArray().toList())
        //send data to groupedSongsInfoViewModel
        val groupingInfo = getGroupingInfo()
        if (groupingInfo != null)
            groupedSongsInfoViewModel.setGroupingInfo(groupingInfo)

        binding = ActivityGroupedSongsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun getSongArray(): Array<SongModel>{
        val bundle = intent.extras
        return bundle?.getParcelableArray(retrievedSongs, SongModel::class.java) ?: emptyArray()
    }

    private fun getGroupingInfo(): GroupingInfoModel? {
        val bundle = intent.extras
        return bundle?.getParcelable(retrievedGroupingInfo, GroupingInfoModel::class.java)
    }

    companion object {
        const val retrievedSongs: String = "retrieved_songs"
        const val retrievedGroupingInfo: String = "retrieved_grouping_info"
    }
}