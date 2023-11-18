package com.example.vibeslocal.di

import com.example.vibeslocal.managers.SongThumbnailManager
import com.example.vibeslocal.managers.SongsQueueManager
import com.example.vibeslocal.repositories.SongsRepository
import com.example.vibeslocal.sources.SongsSource
import com.example.vibeslocal.viewmodels.CurrentPageViewModel
import com.example.vibeslocal.viewmodels.CurrentSongItemViewModel
import com.example.vibeslocal.viewmodels.GroupedSongsInfoViewModel
import com.example.vibeslocal.viewmodels.MainViewModel
import com.example.vibeslocal.viewmodels.MusicItemsViewModel
import com.example.vibeslocal.viewmodels.OptionsViewModel
import com.example.vibeslocal.viewmodels.PlaybackControlViewModel
import com.example.vibeslocal.viewmodels.PlaybackSongActionsViewModel
import com.example.vibeslocal.viewmodels.SongDurationSeekBarViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val viewModelModule = module {
    viewModelOf(::CurrentPageViewModel)
    viewModelOf(::OptionsViewModel)
    viewModelOf(::PlaybackSongActionsViewModel)
    viewModelOf(::CurrentSongItemViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::MusicItemsViewModel)
    viewModelOf(::PlaybackControlViewModel)
    viewModelOf(::GroupedSongsInfoViewModel)
    viewModelOf(::SongDurationSeekBarViewModel)
}


val appModule = module {
    includes(viewModelModule)
    singleOf(::SongsSource)
    singleOf(::SongsRepository)
    singleOf(::SongThumbnailManager)
    singleOf(::SongsQueueManager)
}