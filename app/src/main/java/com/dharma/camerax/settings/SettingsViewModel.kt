package com.dharma.camerax.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dharma.camerax.settings.repo.SettingsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepo: SettingsRepo
) : ViewModel() {

    val isNotificationsEnabled = settingsRepo.isNotificationsEnabled()
    val isDarkThemeEnabled = settingsRepo.isDarkTheme()

    fun setDarkTheme(it: Boolean) {
        viewModelScope.launch {
            settingsRepo.setDarkTheme(it)
        }

    }

    fun setNotificationsEnabled(it: Boolean) {
        viewModelScope.launch {
            settingsRepo.setNotificationsEnabled(it)
        }

    }
}