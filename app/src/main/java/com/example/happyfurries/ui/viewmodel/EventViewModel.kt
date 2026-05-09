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

    fun loadEventsForToday() {
        viewModelScope.launch {
            val today = LocalDate.now().toString()
            _events.value = repository.getEventsByDate(today)
        }
    }

    fun loadEventsForDate(date: LocalDate) {
        viewModelScope.launch {
            val dateString = date.toString()
            _events.value = repository.getEventsByDate(dateString)
        }
    }

    fun addEvent(event: EventEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.insertEvent(event)
            loadEventsForDate(LocalDate.parse(event.date))
            onDone()
        }
    }

    fun deleteEvent(event: EventEntity, onDone: () -> Unit = {}) {
        viewModelScope.launch {
            repository.deleteEvent(event)
            loadEventsForDate(LocalDate.parse(event.date))
            onDone()
        }
    }

    fun eventsForDay(date: LocalDate): List<EventEntity> {
        val dateString = date.toString()
        return _events.value.filter { it.date == dateString }
    }
}
