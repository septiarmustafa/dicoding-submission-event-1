package com.example.dicodingevent.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.local.entity.FavoriteEventEntity
import com.example.dicodingevent.data.repository.EventRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application,  private val eventRepository: EventRepository) : AndroidViewModel(application) {
    val favoriteEventsEntity: LiveData<List<FavoriteEventEntity>> = eventRepository.getAllFavorites().asLiveData()

    fun addFavorite(event: FavoriteEventEntity) = viewModelScope.launch {
        eventRepository.addFavorite(event)
    }

    fun removeFavorite(event: FavoriteEventEntity) = viewModelScope.launch {
        try {
            eventRepository.removeFavorite(event)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun isFavorite(eventId: String): LiveData<Boolean> {
        return eventRepository.isFavorite(eventId)
    }
}