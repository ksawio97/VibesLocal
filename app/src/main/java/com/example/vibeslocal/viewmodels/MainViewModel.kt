package com.example.vibeslocal.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibeslocal.repositories.SongsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.R)
class MainViewModel(private val songsRepository: SongsRepository) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init{
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                songsRepository.loadData()
            }
        }.invokeOnCompletion {
            _isLoading.value = false
        }
    }
}