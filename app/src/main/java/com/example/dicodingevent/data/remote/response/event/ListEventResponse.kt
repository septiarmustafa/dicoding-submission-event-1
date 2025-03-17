package com.example.dicodingevent.data.remote.response.event

import com.example.dicodingevent.data.remote.model.Event
import com.example.dicodingevent.data.remote.response.CommonResponse
import com.google.gson.annotations.SerializedName

data class ListEventResponse(

    @field:SerializedName("commonResponse")
    val commonResponse: CommonResponse,

    @field:SerializedName("listEvents")
    val listEvents: List<Event>? = listOf()

)

