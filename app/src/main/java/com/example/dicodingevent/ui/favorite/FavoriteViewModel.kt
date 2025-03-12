package com.example.dicodingevent.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.local.room.FavoriteDatabase
import com.example.dicodingevent.data.local.entity.FavoriteEventEntity
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val favoriteDao = FavoriteDatabase.getDatabase(application).favoriteDao()
    val favoriteEventsEntity: LiveData<List<FavoriteEventEntity>> = favoriteDao.getAllFavorites().asLiveData()

    fun addFavorite(event: FavoriteEventEntity) = viewModelScope.launch {
        favoriteDao.addFavorite(event)
    }

    fun removeFavorite(event: FavoriteEventEntity) = viewModelScope.launch {
        try {
            favoriteDao.removeFavorite(event)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isFavorite(eventId: String): LiveData<Boolean> {
        return favoriteDao.isFavorite(eventId)
    }
}