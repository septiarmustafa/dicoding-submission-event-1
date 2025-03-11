package com.example.dicodingevent.ui.settings

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore

private val Context.dataStore by preferencesDataStore(name = "settings")

class Settings(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val THEME_KEY = booleanPreferencesKey("theme_setting")
    }

    val themeSetting: Flow<Boolean> = dataStore.data
        .map { preferences -> preferences[THEME_KEY] ?: false }

    suspend fun saveThemeSetting(isDarkMode: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDarkMode
        }
    }
}