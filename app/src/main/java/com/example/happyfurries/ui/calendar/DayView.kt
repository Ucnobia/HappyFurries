package com.example.happyfurries.ui.calendar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.happyfurries.data.entities.EventEntity
import com.example.happyfurries.navigation.Routes
import com.example.happyfurries.ui.components.ConfirmDeleteDialog
import com.example.happyfurries.ui.viewmodel.EventViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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

    val uiState by eventViewModel.uiState.collectAsState()
    val dayEvents = uiState.items.filter { it.date == date.toString() }

    // Evento pendiente de confirmación de borrado (null = sin diálogo)
    var eventToDelete by remember { mutableStateOf<EventEntity?>(null) }

    // Snackbar para mostrar errores
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    val formattedDate = date.format(
        DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.ENGLISH)
    )

    Scaffold(
        snackbarHost   = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding)) {

            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            LazyColumn(
                modifier            = Modifier.fillMaxSize(),
                contentPadding      = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(formattedDate, style = MaterialTheme.typography.titleLarge)
                }

                item {
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

                when {
                    // Estado: cargando
                    uiState.isLoading -> {
                        item {
                            Box(
                                modifier         = Modifier.fillMaxWidth().padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color(0xFF1B5E20))
                            }
                        }
                    }

                    // Estado: error de red
                    uiState.errorMessage != null -> {
                        item {
                            Text(
                                text  = uiState.errorMessage!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    // Estado: lista vacía
                    dayEvents.isEmpty() -> {
                        item {
                            Text("No events for this day", color = Color.Gray)
                        }
                    }

                    // Estado: hay datos
                    else -> {
                        items(dayEvents) { event ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors   = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Row(
                                    modifier          = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(event.time, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                                        Text(event.title, style = MaterialTheme.typography.titleMedium)
                                        if (!event.description.isNullOrBlank()) {
                                            Text(event.description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                        }
                                    }
                                    IconButton(onClick = { navController.navigate(Routes.editEvent(event.id)) }) {
                                        Icon(
                                            imageVector        = Icons.Outlined.Edit,
                                            contentDescription = "Edit event",
                                            tint               = Color(0xFF1B5E20)
                                        )
                                    }
                                    IconButton(onClick = { eventToDelete = event }) {
                                        Icon(
                                            imageVector        = Icons.Outlined.Delete,
                                            contentDescription = "Delete event",
                                            tint               = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(16.dp)) }
            }
        }
    }

    // Diálogo de confirmación de borrado
    eventToDelete?.let { event ->
        ConfirmDeleteDialog(
            title     = "Delete event",
            text      = "Are you sure you want to delete \"${event.title}\"?",
            onConfirm = {
                eventViewModel.deleteEvent(event)
                eventToDelete = null
            },
            onDismiss = { eventToDelete = null }
        )
    }
}