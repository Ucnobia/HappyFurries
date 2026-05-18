package com.example.happyfurries.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.happyfurries.ui.viewmodel.EventViewModel
import java.time.LocalDate
import java.time.YearMonth

// Vista del grid del calendario.
// Usa el EventViewModel para saber qué días tienen eventos
// y marcarlos con un punto debajo del número.

@Composable
fun CalendarView(
    state: CalendarState,
    eventViewModel: EventViewModel,
    onDateSelected: (LocalDate) -> Unit,
    onMonthChange: (YearMonth) -> Unit
) {
    val days = state.currentMonth.getDaysForCalendar()
    val events = eventViewModel.events.collectAsState().value

    Column(modifier = Modifier.fillMaxWidth()) {

        val monthName = state.currentMonth.month
            .getDisplayName(java.time.format.TextStyle.FULL, java.util.Locale("es"))
            .replaceFirstChar { it.uppercase() }

        Text(
            text = "$monthName ${state.currentMonth.year}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        // Cabecera con los días de la semana
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val dayNames = java.time.DayOfWeek.values().map {
                it.getDisplayName(java.time.format.TextStyle.NARROW, java.util.Locale("es"))
                    .replaceFirstChar { c -> c.uppercase() }
            }
            dayNames.forEach { day ->
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    Text(text = day, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        // Grid con los días del mes
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            items(days) { date ->
                val clickedMonth    = YearMonth.from(date)
                val currentMonth    = state.currentMonth
                val isCurrentMonth  = clickedMonth == currentMonth
                // Compruebo si hay algún evento en ese día usando el ViewModel
                val hasEvents       = events.any { it.date == date.toString() }

                Column(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(
                            if (date == state.selectedDate)
                                MaterialTheme.colorScheme.primary
                            else Color.Transparent
                        )
                        .clickable {
                            when {
                                clickedMonth.isBefore(currentMonth) -> onMonthChange(clickedMonth)
                                clickedMonth.isAfter(currentMonth)  -> onMonthChange(clickedMonth)
                                else -> onDateSelected(date)
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = date.dayOfMonth.toString(),
                        color = when {
                            date == state.selectedDate -> MaterialTheme.colorScheme.onPrimary
                            isCurrentMonth            -> MaterialTheme.colorScheme.onBackground
                            else                      -> Color.Gray
                        }
                    )

                    // Punto verde debajo del número si ese día tiene eventos
                    if (hasEvents) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(Color(0xFF4CAF50), CircleShape)
                        )
                    }
                }
            }
        }
    }
}