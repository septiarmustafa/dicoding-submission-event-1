package com.example.dicodingevent.data.retrofit

import com.example.dicodingevent.data.response.event.EventDetailResponse
import com.example.dicodingevent.data.response.event.ListEventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventService {
    @GET("events")
    fun getEvent(@Query("active") active: String, @Query("limit") limit: String? = "40", @Query("q") q: String? = null) : Call<ListEventResponse>

    @GET("/events/{id}")
    fun getEventById(@Path("id") id: String) : Call<EventDetailResponse>
}