package com.example.dicodingevent.ui.event_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.data.remote.response.Result
import com.example.dicodingevent.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EventDetailViewModel(private val repository: EventRepository) : ViewModel() {

    private val _event = MutableStateFlow<Result<Event>>(Result.Loading)
    val event: StateFlow<Result<Event>> = _event

    fun getEventDetail(eventId: String) {
        viewModelScope.launch {
            repository.getEventById(eventId).collect { result ->
                _event.value = result
            }
        }
    }
}