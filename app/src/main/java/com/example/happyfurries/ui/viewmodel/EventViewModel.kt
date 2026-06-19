package com.example.happyfurries.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.happyfurries.data.entities.EventEntity
import com.example.happyfurries.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class EventViewModel(
    private val repository: EventRepository
) : ViewModel() {

    private val _events = MutableStateFlow<List<EventEntity>>(emptyList())
    val events: StateFlow<List<EventEntity>> = _events

    // Estado de la UI — lo observan DayView y PetScheduleScreen
    private val _uiState = MutableStateFlow(EventUiState())
    val uiState: StateFlow<EventUiState> = _uiState

    // Todos los eventos — lo usa el calendario para pintar puntos en todos los días
    private val _allEvents = MutableStateFlow<List<EventEntity>>(emptyList())
    val allEvents: StateFlow<List<EventEntity>> = _allEvents

    // Estado específico de PetScheduleScreen — próximos e historial
    private val _petScheduleState = MutableStateFlow(PetScheduleUiState())
    val petScheduleState: StateFlow<PetScheduleUiState> = _petScheduleState

    // Búsqueda de eventos
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        loadAllEventsForCalendar()
        loadUpcomingEvents()
    }

    // Convierte una fecha String a LocalDate de forma segura
    // Devuelve null si el formato es inválido (evita que la app se cierre)
    private fun parseDateSafely(date: String): LocalDate? {
        return try {
            LocalDate.parse(date)
        } catch (e: Exception) {
            null
        }
    }

    // Carga TODOS los eventos — solo para pintar puntos en el calendario
    fun loadAllEventsForCalendar() {
        viewModelScope.launch {
            try {
                _allEvents.value = repository.getAllEvents()
            } catch (e: Exception) {
                // Si falla, el calendario simplemente no muestra puntos
            }
        }
    }

    // Carga los eventos próximos (hoy en adelante) — los muestra el MainScreen
    fun loadUpcomingEvents() {
        viewModelScope.launch {
            try {
                val today = LocalDate.now()
                val all = repository.getAllEvents()
                _events.value = all
                    .filter { event ->
                        val date = parseDateSafely(event.date)
                        date != null && (date == today || date.isAfter(today))
                    }
                    .sortedWith(compareBy({ it.date }, { it.time }))
            } catch (e: Exception) {
                _events.value = emptyList()
            }
        }
    }

    // Carga los eventos del día actual (lo usa DayView)
    fun loadEventsForToday() {
        viewModelScope.launch {
            val today = LocalDate.now().toString()
            _events.value = repository.getEventsByDate(today)
        }
    }

    // Carga los eventos de una fecha concreta — lo usa el calendario
    fun loadEventsForDate(date: LocalDate) {
        viewModelScope.launch {
            _uiState.value = EventUiState(isLoading = true)
            try {
                val result = repository.getEventsByDate(date.toString())
                _uiState.value = EventUiState(items = result)
                _events.value = result
            } catch (e: Exception) {
                _uiState.value = EventUiState(
                    errorMessage = "Could not load events. Check your connection."
                )
            }
        }
    }

    // Carga todos los eventos de una mascota concreta — lo usa PetScheduleScreen
    fun loadEventsByPet(petId: Int) {
        viewModelScope.launch {
            _uiState.value = EventUiState(isLoading = true)
            try {
                val result = repository.getEventsByPet(petId)
                _uiState.value = EventUiState(items = result)
                _events.value = result
            } catch (e: Exception) {
                _uiState.value = EventUiState(
                    errorMessage = "Could not load events. Check your connection."
                )
            }
        }
    }

    // Carga los eventos de una mascota separados en próximos y pasados
    // Lo usa PetScheduleScreen para las pestañas Upcoming / History
    fun loadPetSchedule(petId: Int) {
        viewModelScope.launch {
            _petScheduleState.value = PetScheduleUiState(isLoading = true)
            try {
                val all = repository.getEventsByPet(petId)
                val today = LocalDate.now()

                // Upcoming = fecha de hoy en adelante (incluye hoy)
                // Past = fechas anteriores a hoy
                val (upcoming, past) = all.partition { event ->
                    val date = parseDateSafely(event.date)
                    date != null && (date == today || date.isAfter(today))
                }

                _petScheduleState.value = PetScheduleUiState(
                    upcomingEvents = upcoming.sortedBy { it.date },
                    pastEvents     = past.sortedByDescending { it.date }
                )
            } catch (e: Exception) {
                _petScheduleState.value = PetScheduleUiState(
                    errorMessage = "Could not load events. Check your connection."
                )
            }
        }
    }

    // Añade un evento y recarga ambas listas
    fun addEvent(event: EventEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertEvent(event)
            parseDateSafely(event.date)?.let { loadEventsForDate(it) }
            loadAllEventsForCalendar()
            loadUpcomingEvents()
            onDone()
        }
    }

    // Actualiza un evento y recarga ambas listas
    fun updateEvent(event: EventEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.updateEvent(event)
            parseDateSafely(event.date)?.let { loadEventsForDate(it) }
            loadAllEventsForCalendar()
            loadUpcomingEvents()
            onDone()
        }
    }

    // Borra un evento y recarga ambas listas
    fun deleteEvent(event: EventEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteEvent(event)
            parseDateSafely(event.date)?.let { loadEventsForDate(it) }
            loadAllEventsForCalendar()
            loadUpcomingEvents()
            onDone()
        }
    }

    // Filtra los eventos ya cargados por fecha — lo usa el calendario
    // para resaltar los días que tienen eventos
    fun eventsForDay(date: LocalDate): List<EventEntity> {
        val dateString = date.toString()
        return _events.value.filter { it.date == dateString }
    }

    // Actualiza el texto de búsqueda
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
}