package com.example.vibeslocal.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupingInfoModel(
    val thumbnailId: Long,
    val title: String,
    val description: String
) : Parcelable