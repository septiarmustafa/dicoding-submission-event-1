package com.example.dicodingevent.di

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.repository.EventRepository
import com.example.dicodingevent.ui.event_detail.EventDetailViewModel
import com.example.dicodingevent.ui.favorite.FavoriteViewModel
import com.example.dicodingevent.ui.finished.FinishedViewModel
import com.example.dicodingevent.ui.home.HomeViewModel
import com.example.dicodingevent.ui.upcoming.UpcomingViewModel

class ViewModelFactory(
    private val eventRepository: EventRepository,
    private val application: Application? = null
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(EventDetailViewModel::class.java) -> {
                EventDetailViewModel(eventRepository) as T
            }

            modelClass.isAssignableFrom(UpcomingViewModel::class.java) -> {
                UpcomingViewModel(eventRepository) as T
            }

            modelClass.isAssignableFrom(FinishedViewModel::class.java) -> {
                FinishedViewModel(eventRepository) as T
            }

            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(eventRepository) as T
            }

            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                application?.let {
                    FavoriteViewModel(it, eventRepository) as T
                } ?: throw IllegalArgumentException("Application is required for FavoriteViewModel")
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}