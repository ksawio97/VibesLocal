package com.example.vibeslocal.di

import com.example.vibeslocal.repositories.SongsRepository
import com.example.vibeslocal.sources.SongsSource
import com.example.vibeslocal.viewmodels.MainViewModel
import com.example.vibeslocal.viewmodels.MusicItemsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { SongsSource(androidContext().contentResolver) }
    single { SongsRepository(get()) }
    viewModelOf(::MainViewModel)
    viewModelOf(::MusicItemsViewModel)
}