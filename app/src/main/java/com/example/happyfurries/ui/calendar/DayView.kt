package com.example.happyfurries.ui.calendar

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.happyfurries.R
import com.example.happyfurries.data.entities.EventEntity
import com.example.happyfurries.ui.AppBackground
import com.example.happyfurries.ui.viewmodel.EventViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Vista del día seleccionado en el calendario.
// Muestra los eventos de ese día y permite añadir o eliminar eventos.

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

    LaunchedEffect(date) {
        eventViewModel.loadEventsForDate(date)
    }

    val allEvents = eventViewModel.events.collectAsState().value
    val dayEvents = allEvents.filter { it.date == date.toString() }

    val formattedDate = date.format(
        DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
    )

    val fieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = Color(0xFFF5F5F5),
        focusedContainerColor   = Color(0xFFF5F5F5)
    )

    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {

            // Cabecera con logo centrado y botón atrás
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .height(56.dp)
            ) {
                IconButton(
                    onClick  = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Image(
                    painter            = painterResource(id = R.drawable.logobig),
                    contentDescription = "Logo",
                    modifier           = Modifier
                        .size(56.dp)
                        .align(Alignment.Center)
                )
            }

            // Contenido con scroll
            LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text  = formattedDate,
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                item {
                    Button(
                        onClick  = { showForm = !showForm },
                        colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (showForm) "Cancel" else "Add event", color = Color.White)
                    }
                }

                if (showForm) {
                    item {
                        OutlinedTextField(
                            value         = title,
                            onValueChange = { title = it },
                            label         = { Text("Title *") },
                            modifier      = Modifier.fillMaxWidth(),
                            colors        = fieldColors
                        )
                    }
                    item {
                        OutlinedTextField(
                            value         = time,
                            onValueChange = { time = it },
                            label         = { Text("Time * (HH:mm)") },
                            modifier      = Modifier.fillMaxWidth(),
                            colors        = fieldColors
                        )
                    }
                    item {
                        OutlinedTextField(
                            value         = description,
                            onValueChange = { description = it },
                            label         = { Text("Description (optional)") },
                            modifier      = Modifier.fillMaxWidth(),
                            colors        = fieldColors
                        )
                    }
                    item {
                        Button(
                            onClick = {
                                if (title.isNotBlank() && time.isNotBlank()) {
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
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
                        ) {
                            Text("Save", color = Color.White)
                        }
                    }
                }

                item {
                    Text("Events:", style = MaterialTheme.typography.titleMedium)
                }

                if (dayEvents.isEmpty()) {
                    item {
                        Text("No events for this day", color = Color.Gray)
                    }
                } else {
                    items(dayEvents) { event ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors   = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier              = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(event.time, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                    Text(event.title, style = MaterialTheme.typography.titleMedium)
                                    if (!event.description.isNullOrBlank()) {
                                        Text(event.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                    }
                                }
                                TextButton(onClick = { eventViewModel.deleteEvent(event) }) {
                                    Text("Delete", color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }
}