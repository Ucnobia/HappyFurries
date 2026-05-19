package com.example.happyfurries.data.dao

import androidx.room.*
import com.example.happyfurries.data.entities.EventEntity

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: EventEntity)

    @Update
    suspend fun updateEvent(event: EventEntity)

    @Delete
    suspend fun deleteEvent(event: EventEntity)

    @Query("SELECT * FROM events WHERE petId = :petId ORDER BY date, time")
    suspend fun getEventsByPet(petId: Int): List<EventEntity>

    @Query("SELECT * FROM events WHERE date = :date ORDER BY time")
    suspend fun getEventsByDate(date: String): List<EventEntity>

    @Query("SELECT * FROM events WHERE petId = :petId AND date = :date ORDER BY time")
    suspend fun getEventsByPetAndDate(petId: Int, date: String): List<EventEntity>
}
