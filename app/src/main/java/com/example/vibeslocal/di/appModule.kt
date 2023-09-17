package com.example.vibeslocal.di

import com.example.vibeslocal.repositories.SongsRepository
import com.example.vibeslocal.services.MediaPlayerService
import com.example.vibeslocal.services.SongsQueueService
import com.example.vibeslocal.sources.SongsSource
import com.example.vibeslocal.viewmodels.MainViewModel
import com.example.vibeslocal.viewmodels.MusicItemsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single { SongsSource(androidContext().contentResolver) }
    singleOf(::SongsRepository)
    singleOf(::SongsQueueService)
    singleOf(::MediaPlayerService)
    viewModelOf(::MainViewModel)
    viewModelOf(::MusicItemsViewModel)
}