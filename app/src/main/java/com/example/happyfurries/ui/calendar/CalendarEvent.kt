package com.example.happyfurries.ui.calendar

import java.time.LocalDate
import java.util.UUID

data class CalendarEvent(
    val id: String = UUID.randomUUID().toString(),
    val date: LocalDate,
    val title: String,
    val description: String? = null
)
