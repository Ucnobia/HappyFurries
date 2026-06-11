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

    init {
        loadEventsForToday()
    }

    // Carga los eventos del día actual al arrancar el ViewModel
    fun loadEventsForToday() {
        viewModelScope.launch {
            val today = LocalDate.now().toString()
            _events.value = repository.getEventsByDate(today)
        }
    }

    // Carga los eventos de una fecha concreta — lo usa el calendario
    fun loadEventsForDate(date: LocalDate) {
        viewModelScope.launch {
            val dateString = date.toString()
            _events.value = repository.getEventsByDate(dateString)
        }
    }

    // Carga todos los eventos de una mascota concreta — lo usa PetScheduleScreen
    fun loadEventsByPet(petId: Int) {
        viewModelScope.launch {
            _events.value = repository.getEventsByPet(petId)
        }
    }

    // Añade un evento y recarga los del día correspondiente
    fun addEvent(event: EventEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertEvent(event)
            loadEventsForDate(LocalDate.parse(event.date))
            onDone()
        }
    }

    // Actualiza un evento y recarga lista correspondiente
    fun updateEvent(event: EventEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.updateEvent(event)
            loadEventsForDate(LocalDate.parse(event.date))
            onDone()
        }
    }

    // Borra un evento y recarga los del día correspondiente
    fun deleteEvent(event: EventEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteEvent(event)
            loadEventsForDate(LocalDate.parse(event.date))
            onDone()
        }
    }

    // Filtra los eventos ya cargados por fecha — lo usa el calendario
    // para resaltar los días que tienen eventos
    fun eventsForDay(date: LocalDate): List<EventEntity> {
        val dateString = date.toString()
        return _events.value.filter { it.date == dateString }
    }
}