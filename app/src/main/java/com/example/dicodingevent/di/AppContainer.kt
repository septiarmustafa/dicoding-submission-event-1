package com.example.dicodingevent.di

import android.content.Context
import com.example.dicodingevent.data.local.room.FavoriteDatabase
import com.example.dicodingevent.data.remote.retrofit.ApiConfig
import com.example.dicodingevent.data.remote.retrofit.EventService
import com.example.dicodingevent.data.repository.EventRepository

class AppContainer(context: Context) {
    private val eventService: EventService = ApiConfig.getApiService()
    private val favoriteDao = FavoriteDatabase.getDatabase(context).favoriteDao()
    val eventRepository: EventRepository = EventRepository(eventService, favoriteDao)
}