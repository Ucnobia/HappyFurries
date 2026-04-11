package com.example.happyfurries.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@Composable
fun DayView(
    date: LocalDate,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = "Día seleccionado: ${date.dayOfMonth}/${date.monthValue}/${date.year}",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Aquí luego añadir:
        // - Lista de eventos
        // - Botón para añadir evento
        // - Detalles del día
        // - Etc.

        Text("Aquí irán los eventos del día…")
    }
}
