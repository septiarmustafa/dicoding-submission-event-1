package com.example.dicodingevent.data.repository

import com.example.dicodingevent.data.local.entity.FavoriteEventEntity
import com.example.dicodingevent.data.local.room.FavoriteDao
import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.data.remote.response.Result
import com.example.dicodingevent.data.remote.retrofit.EventService
import com.example.dicodingevent.shared.EventType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EventRepository(
    private val eventService: EventService,
    private val favoriteDao: FavoriteDao,
) {

    suspend fun getUpcomingEvents(): Flow<Result<List<Event>>> = flow {
        emit(Result.Loading)
        try {
            val response = eventService.getEvent(EventType.UPCOMING.value)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()?.listEvents ?: emptyList()))
            } else {
                emit(Result.Error("Failed to fetch events: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    suspend fun getFinishedEvents(query: String? = null): Flow<Result<List<Event>>> = flow  {
        emit(Result.Loading)
        try {
            val response = eventService.getEvent(EventType.FINISHED.value, q = query)
            if (response.isSuccessful) {
                emit(Result.Success(response.body()?.listEvents ?: emptyList()))
            } else {
                emit(Result.Error("Failed to fetch finished events: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    suspend fun getFinishedEventsHome(): Flow<Result<List<Event>>> = flow{ emit(Result.Loading)
        try {
            val response = eventService.getEvent(EventType.FINISHED.value, limit = "5")
            if (response.isSuccessful) {
                emit(Result.Success(response.body()?.listEvents ?: emptyList()))
            } else {
                emit(Result.Error("Failed to fetch finished events: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unexpected error occurred"))
        }
    }

    suspend fun getUpcomingEventsHome(): Flow<Result<List<Event>>> = flow {
        emit(Result.Loading)
        try {
            val response = eventService.getEvent(EventType.UPCOMING.value, limit = "5")
            if (response.isSuccessful) {
                emit(Result.Success(response.body()?.listEvents ?: emptyList()))
            } else {
                emit(Result.Error("Failed to fetch events: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unexpected error occurred"))
        }
    }


    suspend fun getEventById(eventId: String): Flow<Result<Event>> = flow {
        emit(Result.Loading)
        try {
            val response = eventService.getEventById(eventId)
            if (response.isSuccessful) {
                val event = response.body()?.event
                if (event != null) {
                    emit(Result.Success(event))
                } else {
                    emit(Result.Error("Event data is null"))
                }
            } else {
                emit(Result.Error("Failed to fetch event: ${response.message()}"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "An unexpected error occurred"))
        }
    }

//    fun getAllFavorites() = favoriteDao.getAllFavorites()
    fun getAllFavorites(): Flow<List<FavoriteEventEntity>> = favoriteDao.getAllFavorites()

    suspend fun addFavorite(event: FavoriteEventEntity) {
        favoriteDao.addFavorite(event)
    }

    suspend fun removeFavorite(event: FavoriteEventEntity) {
        favoriteDao.removeFavorite(event)
    }

//    fun isFavorite(eventId: String) = favoriteDao.isFavorite(eventId)
    fun isFavorite(eventId: String): Flow<Boolean> = favoriteDao.isFavorite(eventId)
}