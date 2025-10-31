package com.pthw.food.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pthw.food.domain.repository.CacheRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Created by P.T.H.W on 22/07/2024.
 */
class AppViewModel (
    private val cacheRepository: CacheRepository
) : ViewModel() {
    val appThemeMode = mutableStateOf(cacheRepository.getThemeModeNormal())
    var isSplashShow = mutableStateOf(true)
        private set

    init {
        startSplashScreen()
        getThemeMode()
    }

    private fun startSplashScreen() {
        viewModelScope.launch {
            delay(2000)
            isSplashShow.value = false
        }
    }

    private fun getThemeMode() {
        viewModelScope.launch {
            cacheRepository.getThemeMode().collectLatest {
                appThemeMode.value = it
            }
        }
    }
}