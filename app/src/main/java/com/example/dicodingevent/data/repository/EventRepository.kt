package com.example.dicodingevent.data.repository

import com.example.dicodingevent.data.local.entity.FavoriteEventEntity
import com.example.dicodingevent.data.local.room.FavoriteDao
import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.data.remote.retrofit.EventService
import com.example.dicodingevent.shared.EventType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class EventRepository(private val eventService: EventService, private val favoriteDao: FavoriteDao,) {

    suspend fun getUpcomingEvents(): List<Event> {
        return withContext(Dispatchers.IO) {
            try {
                val response = eventService.getEvent(EventType.UPCOMING.value)
                if (response.isSuccessful) {
                    response.body()?.listEvents ?: emptyList()
                } else {
                    throw Exception("Failed to fetch events: ${response.message()}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to fetch events: ${e.message}")
            }
        }
    }

    suspend fun getFinishedEvents(query: String? = null): List<Event> {
        return withContext(Dispatchers.IO) {
            try {
                val response = eventService.getEvent(EventType.FINISHED.value, q = query)
                if (response.isSuccessful) {
                    response.body()?.listEvents ?: emptyList()
                } else {
                    throw Exception("Failed to fetch finished events: ${response.message()}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to fetch finished events: ${e.message}")
            }
        }
    }

    suspend fun getFinishedEventsHome(): List<Event> {
        return withContext(Dispatchers.IO) {
            try {
                val response = eventService.getEvent(EventType.FINISHED.value, limit = "5")
                if (response.isSuccessful) {
                    response.body()?.listEvents ?: emptyList()
                } else {
                    throw Exception("Failed to fetch finished events: ${response.message()}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to fetch finished events: ${e.message}")
            }
        }
    }

    suspend fun getUpcomingEventsHome(): List<Event> {
        return withContext(Dispatchers.IO) {
            try {
                val response = eventService.getEvent(EventType.UPCOMING.value, limit = "5")
                if (response.isSuccessful) {
                    response.body()?.listEvents ?: emptyList()
                } else {
                    throw Exception("Failed to fetch finished events: ${response.message()}")
                }
            } catch (e: Exception) {
                throw Exception("Failed to fetch finished events: ${e.message}")
            }
        }
    }


    suspend fun getEventById(eventId: String): Event? {
        return try {
            val response = eventService.getEventById(eventId)
            response.body()?.event
        } catch (e: Exception) {
            null
        }
    }

    fun getAllFavorites() = favoriteDao.getAllFavorites()

    suspend fun addFavorite(event: FavoriteEventEntity) {
        favoriteDao.addFavorite(event)
    }

    suspend fun removeFavorite(event: FavoriteEventEntity) {
        favoriteDao.removeFavorite(event)
    }

    fun isFavorite(eventId: String) = favoriteDao.isFavorite(eventId)
}