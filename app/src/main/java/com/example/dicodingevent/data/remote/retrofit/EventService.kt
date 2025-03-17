package com.example.dicodingevent.data.remote.retrofit

import com.example.dicodingevent.data.remote.response.event.EventDetailResponse
import com.example.dicodingevent.data.remote.response.event.ListEventResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EventService {
    @GET("events")
    suspend fun getEvent(
        @Query("active") active: String,
        @Query("limit") limit: String? = "40",
        @Query("q") q: String? = null
    ): Response<ListEventResponse>

    @GET("/events/{id}")
    suspend fun getEventById(
        @Path("id") id: String
    ): Response<EventDetailResponse>

    @GET("events")
    suspend fun getNearestActiveEvent(
        @Query("active") active: Int = -1,
        @Query("limit") limit: Int = 1
    ): Response<ListEventResponse>
}