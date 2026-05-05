package com.example.happyfurries.ui.Main

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.happyfurries.ui.calendar.CalendarEvent
import java.time.LocalDate

@Composable
fun CalendarView(
    state: CalendarState,
    events: List<CalendarEvent>,
    onDateSelected: (LocalDate) -> Unit
) {
    val days = state.currentMonth.getDaysForCalendar()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Título del mes
        Text(
            text = "${state.currentMonth.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${state.currentMonth.year}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )
// Encabezado con los nombres de los días
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val dayNames = listOf("L", "M", "X", "J", "V", "S", "D")
            dayNames.forEach { day ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = day,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Cuadrícula de días
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            items(days) { date ->
                //Eventos en el dia
                val hasEvents = date != null && events.any { it.date == date }
                Column (
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(
                            if (date == state.selectedDate)
                                MaterialTheme.colorScheme.primary
                            else
                                Color.Transparent
                        )
                        .clickable(enabled = date != null) {
                            date?.let(onDateSelected)
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = date?.dayOfMonth?.toString() ?: "",
                        color = if (date == state.selectedDate)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onBackground
                    )
                //Dibujar puntos para eventos
                    if (hasEvents) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .padding(top = 2.dp)
                                .background(Color.Black, CircleShape)
                        )
                    }

                }
            }
        }
    }
}
