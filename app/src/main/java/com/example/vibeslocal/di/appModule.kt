package com.example.vibeslocal.di

import com.example.vibeslocal.repositories.SongsRepository
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.services.SongThumbnailService
import com.example.vibeslocal.services.SongsQueueService
import com.example.vibeslocal.sources.SongsSource
import com.example.vibeslocal.viewmodels.CurrentSongItemViewModel
import com.example.vibeslocal.viewmodels.OptionsViewModel
import com.example.vibeslocal.viewmodels.MainViewModel
import com.example.vibeslocal.viewmodels.MusicItemsViewModel
import com.example.vibeslocal.viewmodels.NavigationTabViewModel
import com.example.vibeslocal.viewmodels.PlaybackControlViewModel
import com.example.vibeslocal.viewmodels.PlaybackSongActionsViewModel
import com.example.vibeslocal.viewmodels.CurrentPageViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::CurrentPageViewModel)
    viewModelOf(::NavigationTabViewModel)
    viewModelOf(::OptionsViewModel)
    viewModelOf(::PlaybackSongActionsViewModel)
    viewModelOf(::CurrentSongItemViewModel)
    viewModelOf(::MainViewModel)
    viewModelOf(::MusicItemsViewModel)
    viewModelOf(::PlaybackControlViewModel)
}

val appModule = module {
    includes(viewModelModule)
    singleOf(::SongThumbnailService)
    singleOf(::SongsSource)
    singleOf(::SongsRepository)
    singleOf(::SongsQueueService)
    singleOf(::MediaPlayerService)
}