package com.example.dicodingevent.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.local.AppDatabase
import com.example.dicodingevent.data.local.favorite_event.FavoriteEvent
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val favoriteDao = AppDatabase.getDatabase(application).favoriteDao()
    val favoriteEvents: LiveData<List<FavoriteEvent>> = favoriteDao.getAllFavorites().asLiveData()

    fun addFavorite(event: FavoriteEvent) = viewModelScope.launch {
        favoriteDao.addFavorite(event)
    }

    fun removeFavorite(event: FavoriteEvent) = viewModelScope.launch {
        try {
            favoriteDao.removeFavorite(event)

            val updatedList = favoriteDao.getAllFavorites().first()
            if (updatedList.isEmpty()) {
                (favoriteEvents as? MutableLiveData)?.postValue(emptyList())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println("Error removing favorite: ${e.message}")
        }
    }

    fun isFavorite(eventId: String): LiveData<Boolean> {
        return favoriteDao.isFavorite(eventId)
    }
}