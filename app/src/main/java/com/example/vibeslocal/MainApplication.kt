package com.example.vibeslocal

import android.app.Application
import com.example.vibeslocal.di.appModule

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import com.example.vibeslocal.viewmodels.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@MainApplication)
            // Load modules
            modules(appModule)
        }
    }
}