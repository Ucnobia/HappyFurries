package com.example.happyfurries.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.happyfurries.ui.viewmodel.EventViewModel
import java.time.YearMonth

// Componente principal del calendario.
// Ahora recibe el EventViewModel para trabajar con eventos reales
// en lugar de una lista local hardcodeada.

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

    // Si hay un día seleccionado mostramos el DayView con sus eventos
    state.selectedDate?.let { selected ->
        DayView(
            date = selected,
            eventViewModel = eventViewModel,
            onBack = { state = state.copy(selectedDate = null) }
        )
        return
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                state = state.copy(currentMonth = state.currentMonth.minusMonths(1))
            }) { Text("<") }

            Button(onClick = {
                state = state.copy(currentMonth = state.currentMonth.plusMonths(1))
            }) { Text(">") }
        }

        CalendarView(
            state = state,
            eventViewModel = eventViewModel,
            onDateSelected = { date ->
                state = state.copy(selectedDate = date)
                // Cargo los eventos de ese día al seleccionarlo
                eventViewModel.loadEventsForDate(date)
            },
            onMonthChange = { newMonth ->
                state = state.copy(currentMonth = newMonth)
            }
        )
    }
}