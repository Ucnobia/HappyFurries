package com.example.happyfurries.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.happyfurries.ui.Main.CalendarState
import com.example.happyfurries.ui.Main.CalendarView
import java.time.YearMonth
import com.example.happyfurries.ui.calendar.CalendarEvent
@Composable
fun Calendar() {
    var state by remember {
        mutableStateOf(
            CalendarState(
                currentMonth = YearMonth.now(),
                selectedDate = null
            )
        )
    }
    var events by remember { mutableStateOf(listOf<CalendarEvent>()) }

    // Si hay un día seleccionado → mostrar vista diaria
    state.selectedDate?.let { selected ->
        DayView(
            date = selected,
            events = events.filter { it.date==selected },
            onAddEvent = {newEvent ->
                events = events + newEvent
            },
            onBack = {
                state = state.copy(selectedDate = null)
            }
        )
        return
    }

    // Si NO hay día seleccionado → mostrar calendario mensual
    Column(modifier = Modifier.fillMaxWidth()) {

        // Navegación entre meses
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                state = state.copy(
                    currentMonth = state.currentMonth.minusMonths(1)
                )
            }) {
                Text("Anterior")
            }

            Button(onClick = {
                state = state.copy(
                    currentMonth = state.currentMonth.plusMonths(1)
                )
            }) {
                Text("Siguiente")
            }
        }

        // Vista del calendario
        CalendarView(
            state = state,
            events = events,
            onDateSelected = { date ->
                state = state.copy(selectedDate = date)
            }
        )
    }
}
