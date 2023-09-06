package com.example.vibeslocal.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.example.vibeslocal.viewmodels.MainViewModel
import com.example.vibeslocal.viewmodels.MusicItemsViewModel

val appModule = module {
    viewModelOf(::MainViewModel)
    viewModelOf(::MusicItemsViewModel)
}