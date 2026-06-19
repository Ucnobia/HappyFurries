package com.example.happyfurries

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.happyfurries.data.dao.EventDao
import com.example.happyfurries.data.entities.EventEntity
import com.example.happyfurries.data.repository.EventRepository
import com.example.happyfurries.ui.viewmodel.EventViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class EventViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepository: FakeEventRepository
    private lateinit var viewModel: EventViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeEventRepository()
        viewModel = EventViewModel(fakeRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // Test 3: loadEventsForDate devuelve solo los eventos del día indicado
    @Test
    fun `loadEventsForDate actualiza la lista con los eventos del dia`() = runTest {
        fakeRepository.addEvent(event(id = 1, date = "2026-06-11", title = "Vet"))
        fakeRepository.addEvent(event(id = 2, date = "2026-06-12", title = "Bath"))

        viewModel.loadEventsForDate(LocalDate.of(2026, 6, 11))
        advanceUntilIdle()

        val result = viewModel.events.value
        assertEquals(1, result.size)
        assertEquals("Vet", result[0].title)
    }

    // Test 4: addEvent inserta el evento en el repositorio
    @Test
    fun `addEvent inserta el evento correctamente`() = runTest {
        val event = event(id = 1, date = "2026-06-11", title = "Grooming")

        viewModel.addEvent(event)
        advanceUntilIdle()

        assertTrue(fakeRepository.events.contains(event))
    }

    // Test 5: deleteEvent elimina el evento del repositorio
    @Test
    fun `deleteEvent elimina el evento correctamente`() = runTest {
        val event = event(id = 1, date = "2026-06-11", title = "Vaccine")
        fakeRepository.addEvent(event)

        viewModel.deleteEvent(event)
        advanceUntilIdle()

        assertFalse(fakeRepository.events.contains(event))
    }

    // Test 6: updateEvent modifica el título del evento existente
    @Test
    fun `updateEvent modifica el evento existente`() = runTest {
        val original = event(id = 1, date = "2026-06-11", title = "Vet")
        val updated  = original.copy(title = "Vet revisión")
        fakeRepository.addEvent(original)

        viewModel.updateEvent(updated)
        advanceUntilIdle()

        val inRepo = fakeRepository.events.find { it.id == 1 }
        assertEquals("Vet revisión", inRepo?.title)
    }

    // Test 7: eventsForDay filtra correctamente por fecha
    @Test
    fun `eventsForDay devuelve solo los eventos del dia indicado`() = runTest {
        fakeRepository.addEvent(event(id = 1, date = "2026-06-11", title = "Vet"))
        fakeRepository.addEvent(event(id = 2, date = "2026-06-12", title = "Bath"))

        viewModel.loadEventsForDate(LocalDate.of(2026, 6, 11))
        advanceUntilIdle()

        val result = viewModel.eventsForDay(LocalDate.of(2026, 6, 11))
        assertEquals(1, result.size)
        assertEquals("Vet", result[0].title)
    }

    // Test 8: si no hay eventos en el día, la lista queda vacía
    @Test
    fun `loadEventsForDate devuelve lista vacia si no hay eventos ese dia`() = runTest {
        fakeRepository.addEvent(event(id = 1, date = "2026-06-11", title = "Vet"))

        viewModel.loadEventsForDate(LocalDate.of(2026, 6, 15))
        advanceUntilIdle()

        val result = viewModel.events.value
        assertTrue(result.isEmpty())
    }

    // Helper para crear eventos de prueba
    private fun event(id: Int, date: String, title: String) = EventEntity(
        id          = id,
        petId       = 1,
        title       = title,
        date        = date,
        time        = "10:00",
        description = null
    )
}

class FakeEventRepository : EventRepository(
    eventDao = object : EventDao {
        override suspend fun insertEvent(event: EventEntity) {}
        override suspend fun updateEvent(event: EventEntity) {}
        override suspend fun deleteEvent(event: EventEntity) {}
        override suspend fun getEventsByPet(petId: Int) = emptyList<EventEntity>()
        override suspend fun getEventsByDate(date: String) = emptyList<EventEntity>()
        override suspend fun getEventsByPetAndDate(petId: Int, date: String) = emptyList<EventEntity>()
        override suspend fun getAllEvents(): List<EventEntity> = emptyList()
    }
) {
    val events = mutableListOf<EventEntity>()

    fun addEvent(event: EventEntity) { events.add(event) }

    override suspend fun getEventsByDate(date: String) =
        events.filter { it.date == date }

    override suspend fun getEventsByPet(petId: Int) =
        events.filter { it.petId == petId }

    override suspend fun getEventsByPetAndDate(petId: Int, date: String) =
        events.filter { it.petId == petId && it.date == date }

    override suspend fun getAllEvents() = events.toList()

    override suspend fun insertEvent(event: EventEntity) { events.add(event) }

    override suspend fun deleteEvent(event: EventEntity) { events.remove(event) }

    override suspend fun updateEvent(event: EventEntity) {
        val index = events.indexOfFirst { it.id == event.id }
        if (index != -1) events[index] = event
    }
}