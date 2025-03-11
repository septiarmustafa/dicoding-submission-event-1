package com.example.dicodingevent.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.ui.settings.Settings

class ViewModelFactory(private val settings: Settings) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Settings::class.java)
            .newInstance(settings)
    }
}