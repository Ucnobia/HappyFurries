package com.example.happyfurries.ui.calendar

import java.time.LocalDate
import java.time.YearMonth

data class CalendarState(
    val currentMonth: YearMonth,
    val selectedDate: LocalDate? = null
)
fun YearMonth.getDaysForCalendar(): List<LocalDate> {
    val firstDay = this.atDay(1)
    val lastDay = this.atEndOfMonth()

    val start = firstDay.minusDays((firstDay.dayOfWeek.value - 1).toLong())
    val end = lastDay.plusDays((7 - lastDay.dayOfWeek.value).toLong())

    return generateSequence(start) { it.plusDays(1) }
        .takeWhile { !it.isAfter(end) }
        .toList()
}

