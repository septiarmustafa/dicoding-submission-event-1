package com.example.dicodingevent.ui.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.dicodingevent.data.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SettingViewModel(private val settingRepository: SettingsRepository) : ViewModel() {
    val themeSetting: LiveData<Boolean> = settingRepository.themeSetting.asLiveData()
    val reminderSetting: LiveData<Boolean> = settingRepository.reminderSetting.asLiveData()

    fun saveThemeSetting(isDarkMode: Boolean) = viewModelScope.launch {
        settingRepository.saveThemeSetting(isDarkMode)
    }

    private fun saveReminderSetting(isEnabled: Boolean) = viewModelScope.launch {
        settingRepository.saveReminderSetting(isEnabled)
    }

    fun scheduleDailyReminder(context: Context, isEnabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            saveReminderSetting(isEnabled)

            val workManager = WorkManager.getInstance(context)

            if (isEnabled) {
                val workRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
                    .setInitialDelay(0, TimeUnit.MILLISECONDS)
                    .build()

                workManager.enqueueUniquePeriodicWork(
                    UNIQUE_WORK_NAME,
                    ExistingPeriodicWorkPolicy.REPLACE,
                    workRequest
                )
            } else {
                workManager.cancelUniqueWork(UNIQUE_WORK_NAME)
            }
        }
    }

    companion object {
        const val UNIQUE_WORK_NAME = "DAILY_REMINDER_WORK_NAME"
    }
}