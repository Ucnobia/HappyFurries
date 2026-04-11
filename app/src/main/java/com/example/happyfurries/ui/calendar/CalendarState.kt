package com.example.happyfurries.ui.Main

import java.time.LocalDate
import java.time.YearMonth

/**
 * Estado del calendario: mes actual y día seleccionado.
 */
data class CalendarState(
    val currentMonth: YearMonth,
    val selectedDate: LocalDate? = null
)

/**
 * Genera la lista de días para pintar el calendario mensual.
 * Los null representan huecos antes del día 1.
 */
fun YearMonth.getDaysForCalendar(): List<LocalDate?> {
    val firstDayOfMonth = atDay(1)
    val firstDayOfWeekIndex = (firstDayOfMonth.dayOfWeek.value % 7) // 0 = domingo
    val totalDays = lengthOfMonth()

    val days = mutableListOf<LocalDate?>()

    // Huecos antes del día 1
    repeat(firstDayOfWeekIndex) {
        days.add(null)
    }

    // Días reales del mes
    for (day in 1..totalDays) {
        days.add(atDay(day))
    }

    return days
}
