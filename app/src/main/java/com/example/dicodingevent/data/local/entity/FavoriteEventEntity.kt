package com.example.dicodingevent.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
data class FavoriteEventEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String
)