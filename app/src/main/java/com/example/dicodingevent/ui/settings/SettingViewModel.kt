package com.example.dicodingevent.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SettingViewModel(private val preferences: SettingsRepository) : ViewModel() {
    val themeSetting: LiveData<Boolean> = preferences.themeSetting.asLiveData()

    fun saveThemeSetting(isDarkMode: Boolean) = viewModelScope.launch {
        preferences.saveThemeSetting(isDarkMode)
    }
}