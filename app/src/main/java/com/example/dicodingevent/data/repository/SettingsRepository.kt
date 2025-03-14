package com.example.dicodingevent.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsRepository(context: Context) {
    private val dataStore = context.dataStore

    val themeSetting: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[THEME_KEY] ?: false }

    val reminderSetting: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[REMINDER_KEY] ?: false }

    suspend fun saveThemeSetting(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkMode
        }
    }

    suspend fun saveReminderSetting(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[REMINDER_KEY] = isEnabled
        }
    }

    companion object {
        val THEME_KEY = booleanPreferencesKey("theme_setting")
        val REMINDER_KEY = booleanPreferencesKey("daily_reminder_enabled")
    }
}