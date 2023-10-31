package com.example.vibeslocal.di

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.vibeslocal.managers.SongThumbnailManager
import com.example.vibeslocal.managers.SongsQueueManager
import com.example.vibeslocal.repositories.SongsRepository
import com.example.vibeslocal.sources.SongsSource
import com.example.vibeslocal.viewmodels.CurrentPageViewModel
import com.example.vibeslocal.viewmodels.CurrentSongItemViewModel
import com.example.vibeslocal.viewmodels.MainViewModel
import com.example.vibeslocal.viewmodels.MusicItemsViewModel
import com.example.vibeslocal.viewmodels.OptionsViewModel
import com.example.vibeslocal.viewmodels.PlaybackControlViewModel
import com.example.vibeslocal.viewmodels.PlaybackSongActionsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

@RequiresApi(Build.VERSION_CODES.R)
val viewModelModule = module {
    viewModelOf(::CurrentPageViewModel)
    viewModelOf(::OptionsViewModel)
    viewModelOf(::PlaybackSongActionsViewModel)
    viewModelOf(::CurrentSongItemViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::MusicItemsViewModel)
    viewModelOf(::PlaybackControlViewModel)
}

@RequiresApi(Build.VERSION_CODES.R)
val appModule = module {
    includes(viewModelModule)
    singleOf(::SongsSource)
    singleOf(::SongsRepository)
    singleOf(::SongThumbnailManager)
    singleOf(::SongsQueueManager)
}