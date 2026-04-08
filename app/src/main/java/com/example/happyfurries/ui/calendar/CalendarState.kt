package com.example.happyfurries.ui.calendar

import java.time.YearMonth
import java.time.LocalDate

data class CalendarState(
    val currentMonth: YearMonth,
    val selectedDate: LocalDate? = null
)

fun YearMonth.getDaysForCalendar(): List<LocalDate?> {
    val firstDayOfMonth = atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val totalDays = lengthOfMonth()

    val days = mutableListOf<LocalDate?>()

    repeat(firstDayOfWeek) {
        days.add(null)
    }

    for (day in 1..totalDays) {
        days.add(atDay(day))
    }

    return days
}
