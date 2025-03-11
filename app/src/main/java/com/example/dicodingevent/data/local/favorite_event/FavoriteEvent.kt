package com.example.dicodingevent.data.local.favorite_event

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
data class FavoriteEvent(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String
)