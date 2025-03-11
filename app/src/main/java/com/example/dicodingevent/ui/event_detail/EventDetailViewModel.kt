package com.example.dicodingevent.ui.event_detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.data.remote.response.event.EventDetailResponse
import com.example.dicodingevent.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventDetailViewModel : ViewModel() {
    private val _event = MutableLiveData<Event>()
    val event : LiveData<Event> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    companion object{
        private const val TAG = "EventDetailViewModel"
    }

    fun getEventDetail(eventId: String) {
        _isLoading.postValue(true)

        val client = ApiConfig.getApiService().getEventById(eventId)
        client.enqueue(object : Callback<EventDetailResponse> {

            override fun onResponse(
                call: Call<EventDetailResponse>,
                response: Response<EventDetailResponse>
            ) {
                _isLoading.postValue(false)
                if (response.isSuccessful) {
                    _event.postValue(response.body()?.event ?: Event())
                }  else {
                    val errorMsg = response.errorBody()?.string() ?: response.message()
                    _errorMessage.postValue(errorMsg)
                    Log.e(TAG, "onFailure ${response.body()?.commonResponse?.error}: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventDetailResponse>, t: Throwable) {
                _isLoading.postValue(false)
                _errorMessage.postValue("Request failed: ${t.localizedMessage}")
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}