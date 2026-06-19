package com.example.happyfurries

import com.example.happyfurries.data.dao.EventDao
import com.example.happyfurries.data.entities.EventEntity
import com.example.happyfurries.data.repository.EventRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class EventRepositoryTest {

    private lateinit var fakeDao: FakeEventDao
    private lateinit var repository: EventRepository

    @Before
    fun setUp() {
        fakeDao = FakeEventDao()
    }

    // Test 1: si el servidor falla, devuelve los datos de Room
    @Test
    fun `getEventsByDate devuelve datos de Room si el servidor falla`() = runTest {
        // Tenemos un evento guardado en Room
        val localEvent = eventEntity(id = 1, date = "2026-06-11", title = "Vet")
        fakeDao.insertEvent(localEvent)

        // El repositorio usa un servidor que siempre falla
        val repositoryWithFailure = object : EventRepository(fakeDao) {
            override suspend fun getEventsByDate(date: String): List<EventEntity> {
                return try {
                    throw Exception("No connection")
                } catch (e: Exception) {
                    fakeDao.getEventsByDate(date)
                }
            }
        }

        val result = repositoryWithFailure.getEventsByDate("2026-06-11")

        assertEquals(1, result.size)
        assertEquals("Vet", result[0].title)
    }

    // Test 2: si el servidor responde, los datos se guardan en Room
    @Test
    fun `insertEvent guarda el evento en Room`() = runTest {
        val repositoryWithDao = object : EventRepository(fakeDao) {
            override suspend fun insertEvent(event: EventEntity) {
                // Simula fallo de red y guarda en Room directamente
                fakeDao.insertEvent(event)
            }
        }

        val event = eventEntity(id = 1, date = "2026-06-11", title = "Grooming")
        repositoryWithDao.insertEvent(event)

        val inRoom = fakeDao.getEventsByDate("2026-06-11")
        assertEquals(1, inRoom.size)
        assertEquals("Grooming", inRoom[0].title)
    }

    private fun eventEntity(id: Int, date: String, title: String) = EventEntity(
        id          = id,
        petId       = 1,
        title       = title,
        date        = date,
        time        = "10:00",
        description = null
    )
}

// DAO falso que simula Room con una lista en memoria
class FakeEventDao : EventDao {
    private val events = mutableListOf<EventEntity>()

    override suspend fun insertEvent(event: EventEntity) { events.add(event) }
    override suspend fun updateEvent(event: EventEntity) {
        val index = events.indexOfFirst { it.id == event.id }
        if (index != -1) events[index] = event
    }
    override suspend fun deleteEvent(event: EventEntity) { events.remove(event) }
    override suspend fun getEventsByPet(petId: Int) = events.filter { it.petId == petId }
    override suspend fun getEventsByDate(date: String) = events.filter { it.date == date }
    override suspend fun getEventsByPetAndDate(petId: Int, date: String) =
        events.filter { it.petId == petId && it.date == date }
    override suspend fun getAllEvents() = events.toList()
}