package com.example.dicodingevent.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.dicodingevent.data.local.entity.FavoriteEventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(event: FavoriteEventEntity)

    @Delete
    suspend fun removeFavorite(event: FavoriteEventEntity)

    @Query("SELECT * FROM favorite_events")
    fun getAllFavorites(): Flow<List<FavoriteEventEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_events WHERE id = :eventId)")
    fun isFavorite(eventId: String): Flow<Boolean>
}