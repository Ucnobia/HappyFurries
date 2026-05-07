package com.example.happyfurries.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.happyfurries.ui.calendar.CalendarState
import com.example.happyfurries.ui.calendar.CalendarView
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

    state.selectedDate?.let { selected ->
        DayView(
            date = selected,
            events = events.filter { it.date == selected },
            onAddEvent = { newEvent -> events = events + newEvent },
            onDeleteEvent = { event -> events = events - event },
            onBack = { state = state.copy(selectedDate = null) }
        )
        return
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                state = state.copy(currentMonth = state.currentMonth.minusMonths(1))
            }) { Text("Anterior") }

            Button(onClick = {
                state = state.copy(currentMonth = state.currentMonth.plusMonths(1))
            }) { Text("Siguiente") }
        }

        CalendarView(
            state = state,
            events = events,
            onDateSelected = { date ->
                state = state.copy(selectedDate = date)
            },
            onMonthChange = { newMonth ->
                state = state.copy(currentMonth = newMonth)
            }
        )
    }
}

