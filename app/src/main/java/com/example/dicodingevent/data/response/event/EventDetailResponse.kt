package com.example.dicodingevent.data.response.event

import com.example.dicodingevent.data.model.Event
import com.example.dicodingevent.data.response.CommonResponse
import com.google.gson.annotations.SerializedName

data class EventDetailResponse(
    @field:SerializedName("commonResponse")
    val commonResponse: CommonResponse,

    @field:SerializedName("event")
    val event: Event? = Event()
)
