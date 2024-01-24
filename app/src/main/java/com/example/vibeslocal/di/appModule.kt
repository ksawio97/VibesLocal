package com.example.vibeslocal.di

import com.example.vibeslocal.managers.SongThumbnailManager
import com.example.vibeslocal.managers.SongsQueueManager
import com.example.vibeslocal.repositories.SongsRepository
import com.example.vibeslocal.sources.SongsSource
import com.example.vibeslocal.viewmodels.CurrentPageViewModel
import com.example.vibeslocal.viewmodels.CurrentSongInfoViewModel
import com.example.vibeslocal.viewmodels.CurrentSongItemViewModel
import com.example.vibeslocal.viewmodels.GroupedSongsInfoViewModel
import com.example.vibeslocal.viewmodels.MainViewModel
import com.example.vibeslocal.viewmodels.MusicItemsViewModel
import com.example.vibeslocal.viewmodels.OptionsViewModel
import com.example.vibeslocal.viewmodels.PlaybackControl2ViewModel
import com.example.vibeslocal.viewmodels.PlaybackControlViewModel
import com.example.vibeslocal.viewmodels.SongDurationSeekBarViewModel
import com.example.vibeslocal.viewmodels.QueueInfoViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val viewModelModule = module {
    viewModelOf(::CurrentPageViewModel)
    viewModelOf(::OptionsViewModel)
    viewModelOf(::PlaybackControl2ViewModel)
    viewModelOf(::CurrentSongItemViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::MusicItemsViewModel)
    viewModelOf(::PlaybackControlViewModel)
    viewModelOf(::GroupedSongsInfoViewModel)
    viewModelOf(::SongDurationSeekBarViewModel)
    viewModelOf(::CurrentSongInfoViewModel)
    viewModelOf(::QueueInfoViewModel)
}


val appModule = module {
    includes(viewModelModule)
    singleOf(::SongsSource)
    singleOf(::SongsRepository)
    singleOf(::SongThumbnailManager)
    singleOf(::SongsQueueManager)
}