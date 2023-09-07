package com.example.vibeslocal.di

import com.example.vibeslocal.services.AudioFilesService
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.example.vibeslocal.viewmodels.MainViewModel
import com.example.vibeslocal.viewmodels.MusicItemsViewModel

val appModule = module {
    single { AudioFilesService() }
    viewModelOf(::MainViewModel)
    viewModelOf(::MusicItemsViewModel)
}