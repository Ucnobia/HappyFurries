package com.example.happyfurries.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DayView(
    date: LocalDate,
    events: List<CalendarEvent>,
    onAddEvent: (CalendarEvent) -> Unit,
    onDeleteEvent: (CalendarEvent) -> Unit,
    onBack: () -> Unit
) {
    var showForm by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        val formattedDate = date.format(
            DateTimeFormatter.ofPattern("d 'de' MMMM yyyy", Locale("es"))
        )


        // Título del día
        Text("Día seleccionado: $formattedDate" +
                "")

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver
        Button(onClick = onBack) {
            Text("Volver")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para mostrar formulario
        Button(onClick = { showForm = true }) {
            Text("Añadir evento")
        }

        // FORMULARIO
        if (showForm) {
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Título del evento") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción (opcional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        onAddEvent(
                            CalendarEvent(
                                date = date,
                                title = title,
                                description = description.ifBlank { null }
                            )
                        )
                        // limpiar formulario
                        title = ""
                        description = ""
                        showForm = false
                    }
                }
            ) {
                Text("Guardar")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Lista de eventos
        Text("Eventos del día:")

        if (events.isEmpty()) {
            Text("No hay eventos para este día")
        } else {
            events.forEach { event ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("- ${event.title}")

                    Button(onClick = { onDeleteEvent(event) }) {
                        Text("Eliminar")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
