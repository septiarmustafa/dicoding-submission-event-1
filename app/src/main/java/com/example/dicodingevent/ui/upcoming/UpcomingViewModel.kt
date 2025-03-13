package com.example.dicodingevent.ui.upcoming

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.data.remote.response.Result
import com.example.dicodingevent.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UpcomingViewModel(private val repository: EventRepository) : ViewModel() {

    private val _upcomingEvents = MutableStateFlow<Result<List<Event>>>(Result.Loading)
    val upcomingEvents: StateFlow<Result<List<Event>>> = _upcomingEvents

    fun getUpcomingEvents() {
        viewModelScope.launch {
            repository.getUpcomingEvents().collect {
                _upcomingEvents.value = it
            }
        }
    }
}