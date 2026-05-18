package com.example.happyfurries.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.happyfurries.data.entities.EventEntity
import com.example.happyfurries.ui.viewmodel.EventViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Vista del día seleccionado en el calendario.
// Muestra los eventos de ese día y permite añadir o eliminar eventos
// usando el EventViewModel en lugar de una lista local.

@Composable
fun DayView(
    date: LocalDate,
    eventViewModel: EventViewModel,
    onBack: () -> Unit
) {
    var showForm    by remember { mutableStateOf(false) }
    var title       by remember { mutableStateOf("") }
    var time        by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    // Cargo los eventos de este día al abrir el DayView
    LaunchedEffect(date) {
        eventViewModel.loadEventsForDate(date)
    }

    val allEvents = eventViewModel.events.collectAsState().value
    // Filtro solo los eventos de este día concreto
    val dayEvents = allEvents.filter { it.date == date.toString() }

    Column(modifier = Modifier.padding(16.dp)) {

        val formattedDate = date.format(
            DateTimeFormatter.ofPattern("d 'de' MMMM yyyy", Locale("es"))
        )

        Text(
            text = formattedDate,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) { Text("Back") }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { showForm = true }) { Text("Add event") }

        // Formulario para añadir un evento nuevo
        if (showForm) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title *") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Time * (HH:mm)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (title.isNotBlank() && time.isNotBlank()) {
                        // Creo el EventEntity con los datos del formulario
                        // petId = 0 porque el evento se crea desde el calendario
                        // sin seleccionar mascota concreta
                        val newEvent = EventEntity(
                            id          = 0,
                            petId       = 0,
                            title       = title.trim(),
                            date        = date.toString(),
                            time        = time.trim(),
                            description = description.trim().ifBlank { null }
                        )
                        eventViewModel.addEvent(newEvent)
                        title       = ""
                        time        = ""
                        description = ""
                        showForm    = false
                    }
                }
            ) {
                Text("Save")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Events:", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        if (dayEvents.isEmpty()) {
            Text("No events for this day", color = androidx.compose.ui.graphics.Color.Gray)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(dayEvents) { event ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = androidx.compose.ui.graphics.Color.White
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(event.time, style = MaterialTheme.typography.labelSmall)
                                Text(event.title, style = MaterialTheme.typography.titleMedium)
                                if (!event.description.isNullOrBlank()) {
                                    Text(event.description, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                            TextButton(onClick = { eventViewModel.deleteEvent(event) }) {
                                Text("Delete", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}