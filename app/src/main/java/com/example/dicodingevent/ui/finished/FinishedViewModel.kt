package com.example.dicodingevent.ui.finished

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.data.remote.response.Result
import com.example.dicodingevent.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FinishedViewModel(private val repository: EventRepository) : ViewModel() {

    private val _finishedEvents = MutableStateFlow<Result<List<Event>>>(Result.Loading)
    val finishedEvents: StateFlow<Result<List<Event>>> = _finishedEvents

    fun getFinishedEvents(query: String? = null) {
        viewModelScope.launch {
            repository.getFinishedEvents(query).collect {
                _finishedEvents.value = it
            }
        }
    }

//    private val _listEvent = MutableLiveData<List<Event>>()
//    val listEvent : LiveData<List<Event>> = _listEvent
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    private val _errorMessage = MutableLiveData<String?>()
//    val errorMessage: LiveData<String?> = _errorMessage
//
//    init{
//        getEvent()
//    }
//
//    fun getEvent(query: String? = null) {
//        viewModelScope.launch {
//            try {
//                _isLoading.value = true
//                _listEvent.value = repository.getFinishedEvents(query)
//            } catch (e: Exception) {
//                _errorMessage.value = e.message ?: "Unknown error occurred"
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//
//    fun clearErrorMessage() {
//        _errorMessage.value = null
//    }
}