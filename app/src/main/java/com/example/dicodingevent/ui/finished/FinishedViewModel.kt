package com.example.dicodingevent.ui.finished

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

class FinishedViewModel : ViewModel() {

    private val _listEvent = MutableLiveData<List<Event>>()
    val listEvent : LiveData<List<Event>> = _listEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    companion object{
        private const val TAG = "FinishedViewModel"
    }

    init{
        getEvent()
    }

    fun getEvent(query: String? = null) {
        _isLoading.postValue(true)

        val client = ApiConfig.getApiService().getEvent(EventType.FINISHED.value, q = query)
        client.enqueue(object : Callback<ListEventResponse> {
            override fun onResponse(
                call: Call<ListEventResponse>,
                response: Response<ListEventResponse>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    _listEvent.value = response.body()?.listEvents ?: emptyList()
                }  else {
                    _errorMessage.value = response.message()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ListEventResponse>, t: Throwable) {
                _isLoading.postValue(false)
                _errorMessage.value = "Sorry, the request cannot be processed"
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}