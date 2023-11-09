package com.example.vibeslocal.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.example.vibeslocal.managers.SongThumbnailManager
import com.example.vibeslocal.models.GroupingInfoModel

class GroupedSongsInfoViewModel(private val songThumbnailManager: SongThumbnailManager) : ViewModel() {
    private var groupingInfo: GroupingInfoModel? = null

    fun setGroupingInfo(groupingModel: GroupingInfoModel) {
        this.groupingInfo = groupingModel
    }

    fun getGroupingInfo(): GroupingInfoModel? = groupingInfo
    fun getThumbnail(albumId: Long): Bitmap = songThumbnailManager.getThumbnail(albumId)
}