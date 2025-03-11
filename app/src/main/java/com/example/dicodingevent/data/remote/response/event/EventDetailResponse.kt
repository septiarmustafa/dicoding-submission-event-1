package com.example.dicodingevent.data.remote.response.event

import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.data.remote.response.CommonResponse
import com.google.gson.annotations.SerializedName

data class EventDetailResponse(
    @field:SerializedName("commonResponse")
    val commonResponse: CommonResponse,

    @field:SerializedName("event")
    val event: Event? = Event()
)
