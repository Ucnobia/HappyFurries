package com.example.happyfurries.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.happyfurries.ui.viewmodel.EventViewModel
import java.time.YearMonth
import java.util.Locale

@Composable
fun Calendar(eventViewModel: EventViewModel) {

    var state by remember {
        mutableStateOf(
            CalendarState(
                currentMonth = YearMonth.now(),
                selectedDate = null
            )
        )
    }

    state.selectedDate?.let { selected ->
        DayView(
            date           = selected,
            eventViewModel = eventViewModel,
            onBack         = { state = state.copy(selectedDate = null) }
        )
        return
    }

    // Nombre del mes formateado
    val monthName = state.currentMonth.month
        .getDisplayName(java.time.format.TextStyle.FULL, Locale.ENGLISH)
        .replaceFirstChar { it.uppercase() }

    Column(modifier = Modifier.fillMaxWidth()) {

        // Flecha izquierda | Mes + año | Flecha derecha
        Row(
            modifier              = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment     = Alignment.CenterVertically
        ) {
            Button(onClick = {
                state = state.copy(currentMonth = state.currentMonth.minusMonths(1))
            }) { Text("<") }

            // Mes y año centrados
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text      = monthName,
                    style     = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center
                )
                Text(
                    text      = state.currentMonth.year.toString(),
                    style     = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color     = androidx.compose.ui.graphics.Color.Gray
                )
            }

            Button(onClick = {
                state = state.copy(currentMonth = state.currentMonth.plusMonths(1))
            }) { Text(">") }
        }

        CalendarView(
            state          = state,
            eventViewModel = eventViewModel,
            onDateSelected = { date ->
                state = state.copy(selectedDate = date)
                eventViewModel.loadEventsForDate(date)
            },
            onMonthChange  = { newMonth ->
                state = state.copy(currentMonth = newMonth)
            }
        )
    }
}