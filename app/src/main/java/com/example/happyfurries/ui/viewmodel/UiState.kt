package com.example.happyfurries.ui.viewmodel

import com.example.happyfurries.data.entities.EventEntity

// Estados de UI --> Eventos
data class EventUiState(
    val isLoading:    Boolean          = false,
    val errorMessage: String?          = null,
    val items:        List<EventEntity> = emptyList()
)

// Estados UI--> Eventos pasados
data class PetScheduleUiState(
    val upcomingEvents: List<EventEntity> = emptyList(),
    val pastEvents:     List<EventEntity> = emptyList(),
    val isLoading:      Boolean           = false,
    val errorMessage:   String?           = null
)