package com.example.dicodingevent.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.data.remote.response.event.ListEventResponse
import com.example.dicodingevent.data.remote.retrofit.ApiConfig
import com.example.dicodingevent.shared.EventType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _upcomingEvents = MutableLiveData<List<Event>>()
    val upcomingEvents: LiveData<List<Event>> get() = _upcomingEvents

    private val _finishedEvents = MutableLiveData<List<Event>>()
    val finishedEvents: LiveData<List<Event>> get() = _finishedEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    companion object{
        private const val TAG = "HomeViewModel"
        private const val LIMIT_EVENT_HOME = "5"
    }

    init {
        loadAllEvent()
    }

    fun loadAllEvent(){
        loadEvents(EventType.UPCOMING.value, _upcomingEvents)
        loadEvents(EventType.FINISHED.value, _finishedEvents)
    }

    private fun loadEvents(active: String, liveData: MutableLiveData<List<Event>>) {
        _isLoading.postValue(true)

        val client = ApiConfig.getApiService().getEvent(active, LIMIT_EVENT_HOME)
        client.enqueue(object : Callback<ListEventResponse> {
            override fun onResponse(
                call: Call<ListEventResponse>,
                response: Response<ListEventResponse>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    liveData.value = response.body()?.listEvents ?: emptyList()
                } else {
                    _errorMessage.value = response.message()
                    Log.e(TAG, "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ListEventResponse>, t: Throwable) {
                _isLoading.postValue(false)
                _errorMessage.value = "Sorry, the request cannot be processed"
                Log.e(TAG, "Error: ${t.message}")
            }
        })
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}