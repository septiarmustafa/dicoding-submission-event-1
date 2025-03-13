package com.example.dicodingevent.ui.event_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.data.remote.response.Result
import com.example.dicodingevent.data.remote.response.event.EventDetailResponse
import com.example.dicodingevent.data.remote.retrofit.ApiConfig
import com.example.dicodingevent.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventDetailViewModel(private val repository: EventRepository) : ViewModel() {
//    private val _event = MutableLiveData<Event>()
//    val event : LiveData<Event> = _event
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    private val _errorMessage = MutableLiveData<String?>()
//    val errorMessage: LiveData<String?> = _errorMessage
    private val _event = MutableStateFlow<Result<Event>>(Result.Loading)
    val event: StateFlow<Result<Event>> = _event

    fun getEventDetail(eventId: String) {
        viewModelScope.launch {
            repository.getEventById(eventId).collect { result ->
                _event.value = result
            }
        }
    }

//    fun getEventDetail(eventId: String) {
//        viewModelScope.launch {
//            _isLoading.value = true
//            try {
//                val eventDetail = repository.getEventById(eventId)
//                _event.value = eventDetail ?: Event()
//            } catch (e: Exception) {
//                _errorMessage.value = "Failed to fetch event: ${e.localizedMessage}"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

//    fun clearErrorMessage() {
//        _errorMessage.value = null
//    }
}