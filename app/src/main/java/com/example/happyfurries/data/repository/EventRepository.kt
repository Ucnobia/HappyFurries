package com.example.happyfurries.data.repository

import com.example.happyfurries.data.dao.EventDao
import com.example.happyfurries.data.entities.EventEntity

class EventRepository(
    private val eventDao: EventDao
) {

    suspend fun insertEvent(event: EventEntity) =
        eventDao.insertEvent(event)

    suspend fun updateEvent(event: EventEntity) =
        eventDao.updateEvent(event)

    suspend fun deleteEvent(event: EventEntity) =
        eventDao.deleteEvent(event)

    suspend fun getEventsByPet(petId: Int): List<EventEntity> =
        eventDao.getEventsByPet(petId)

    suspend fun getEventsByDate(date: String): List<EventEntity> =
        eventDao.getEventsByDate(date)

    suspend fun getEventsByPetAndDate(petId: Int, date: String): List<EventEntity> =
        eventDao.getEventsByPetAndDate(petId, date)
}
