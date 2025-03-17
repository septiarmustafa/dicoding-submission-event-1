package com.example.dicodingevent.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.data.remote.response.Result
import com.example.dicodingevent.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: EventRepository) : ViewModel() {

    private val _upcomingEvents = MutableStateFlow<Result<List<Event>>>(Result.Loading)
    val upcomingEvents: StateFlow<Result<List<Event>>> get() = _upcomingEvents

    private val _finishedEvents = MutableStateFlow<Result<List<Event>>>(Result.Loading)
    val finishedEvents: StateFlow<Result<List<Event>>> get() = _finishedEvents

    init {
        loadAllEvents()
    }


    fun loadAllEvents() {
        viewModelScope.launch {
            _upcomingEvents.value = Result.Loading
            _finishedEvents.value = Result.Loading

            launch {
                try {
                    repository.getUpcomingEventsHome().collect { result ->
                        _upcomingEvents.value = result
                    }
                } catch (e: Exception) {
                    _upcomingEvents.value =
                        Result.Error(e.message ?: "Failed to load upcoming events")
                }
            }

            launch {
                try {
                    repository.getFinishedEventsHome().collect { result ->
                        _finishedEvents.value = result
                    }
                } catch (e: Exception) {
                    _finishedEvents.value =
                        Result.Error(e.message ?: "Failed to load finished events")
                }
            }
        }
    }
}