package com.example.happyfurries.data.repository

import com.example.happyfurries.data.dao.EventDao
import com.example.happyfurries.data.entities.EventEntity
import com.example.happyfurries.data.network.ApiClient
import com.example.happyfurries.data.network.EventApiModel
import com.example.happyfurries.data.network.toEntity

// Igual que PetRepository pero para eventos.
// Sincroniza con el backend y guarda en Room como copia local.

open class EventRepository(
    private val eventDao: EventDao
) {
    private val api = ApiClient.service

    // Pide los eventos al servidor filtrando por mascota y/o fecha
    // Si el servidor falla devuelvo lo que tenga en Room
    open suspend fun getEventsByDate(date: String): List<EventEntity> {
        return try {
            val eventsFromServer = api.getEvents(date = date)
            eventsFromServer.forEach { eventDao.insertEvent(it.toEntity()) }
            eventDao.getEventsByDate(date)
        } catch (e: Exception) {
            eventDao.getEventsByDate(date)
        }
    }

    // Pide los eventos de una mascota concreta
    open suspend fun getEventsByPet(petId: Int): List<EventEntity> {
        return try {
            val eventsFromServer = api.getEvents(petId = petId)
            eventsFromServer.forEach { eventDao.insertEvent(it.toEntity()) }
            eventDao.getEventsByPet(petId)
        } catch (e: Exception) {
            eventDao.getEventsByPet(petId)
        }
    }

    // Pide eventos filtrando por mascota y fecha a la vez
    open suspend fun getEventsByPetAndDate(petId: Int, date: String): List<EventEntity> {
        return try {
            val eventsFromServer = api.getEvents(petId = petId, date = date)
            eventsFromServer.forEach { eventDao.insertEvent(it.toEntity()) }
            eventDao.getEventsByPetAndDate(petId, date)
        } catch (e: Exception) {
            eventDao.getEventsByPetAndDate(petId, date)
        }
    }

    // Pide TODOS los eventos del servidor (sin filtro)
    open suspend fun getAllEvents(): List<EventEntity> {
        return try {
            val eventsFromServer = api.getEvents()
            eventsFromServer.forEach { eventDao.insertEvent(it.toEntity()) }
            eventDao.getAllEvents()
        } catch (e: Exception) {
            eventDao.getAllEvents()
        }
    }

    // Crea un evento en el servidor y lo guarda en Room
    open suspend fun insertEvent(event: EventEntity) {
        try {
            val eventFromServer = api.createEvent(event.toApiModel())
            eventDao.insertEvent(eventFromServer.toEntity())
        } catch (e: Exception) {
            eventDao.insertEvent(event)
        }
    }

    // Actualiza un evento en el servidor y Room
    open suspend fun updateEvent(event: EventEntity) {
        try {
            api.updateEvent(event.id, event.toApiModel())
            eventDao.updateEvent(event)
        } catch (e: Exception) {
            eventDao.updateEvent(event)
        }
    }

    // Borra un evento del servidor y de Room
    open suspend fun deleteEvent(event: EventEntity) {
        try {
            api.deleteEvent(event.id)
            eventDao.deleteEvent(event)
        } catch (e: Exception) {
            eventDao.deleteEvent(event)
        }
    }
}

// Convierte EventEntity (Room) a EventApiModel (red) para mandarlo al servidor
fun EventEntity.toApiModel() = EventApiModel(
    id          = this.id,
    petId       = this.petId,
    title       = this.title,
    date        = this.date,
    time        = this.time,
    description = this.description
)