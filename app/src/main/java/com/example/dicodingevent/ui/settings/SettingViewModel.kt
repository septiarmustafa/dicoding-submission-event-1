package com.example.dicodingevent.ui.settings

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
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
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiresBatteryNotLow(true)
                            .setRequiresCharging(false)
                            .setRequiresDeviceIdle(false)
                            .build()
                    )
                    .build()

                workManager.enqueueUniquePeriodicWork(
                    "daily_reminder_work",
                    ExistingPeriodicWorkPolicy.REPLACE,
                    workRequest
                )
            } else {
                workManager.cancelUniqueWork("daily_reminder_work")
            }
        }
    }
}