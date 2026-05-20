package com.example.happyfurries.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.happyfurries.navigation.Routes
import com.example.happyfurries.ui.viewmodel.EventViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// Vista del día seleccionado en el calendario.
// El botón Add event navega a AddEventScreen pasando la fecha seleccionada.

@Composable
fun DayView(
    date: LocalDate,
    eventViewModel: EventViewModel,
    navController: NavController,
    onBack: () -> Unit
) {
    LaunchedEffect(date) {
        eventViewModel.loadEventsForDate(date)
    }

    val allEvents = eventViewModel.events.collectAsState().value
    val dayEvents = allEvents.filter { it.date == date.toString() }

    val formattedDate = date.format(
        DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
    )

    Column(modifier = Modifier.fillMaxSize()) {

        // Flecha atrás
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }

        LazyColumn(
            modifier            = Modifier.fillMaxSize(),
            contentPadding      = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text  = formattedDate,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
                // Navega a AddEventScreen pasando la fecha del día seleccionado
                Button(
                    onClick  = { navController.navigate(Routes.addEvent(date = date.toString())) },
                    colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Add event", color = Color.White)
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