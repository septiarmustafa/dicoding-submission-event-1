package com.example.dicodingevent.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.repository.SettingsRepository

class SettingViewModelFactory(private val settingRepository: SettingsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SettingsRepository::class.java)
            .newInstance(settingRepository)
    }
}