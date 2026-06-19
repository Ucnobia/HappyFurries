package com.example.happyfurries.ui.pet

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.happyfurries.R
import com.example.happyfurries.data.entities.EventEntity
import com.example.happyfurries.navigation.Routes
import com.example.happyfurries.ui.AppBackground
import com.example.happyfurries.ui.components.ConfirmDeleteDialog
import com.example.happyfurries.ui.viewmodel.EventViewModel
import com.example.happyfurries.ui.viewmodel.PetViewModel

@Composable
fun PetScheduleScreen(
    petId: Int,
    petViewModel: PetViewModel,
    eventViewModel: EventViewModel,
    navController: NavController,
    onBack: () -> Unit
) {
    LaunchedEffect(petId) {
        eventViewModel.loadPetSchedule(petId)
        petViewModel.loadPets()
    }

    val scheduleState by eventViewModel.petScheduleState.collectAsState()
    val pets          = petViewModel.pets.collectAsState().value
    val pet           = pets.find { it.id == petId }

    var selectedTab   by remember { mutableStateOf(0) }

    // Evento pendiente de confirmación de borrado (null = sin diálogo)
    var eventToDelete by remember { mutableStateOf<EventEntity?>(null) }

    // Snackbar para mostrar errores
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(scheduleState.errorMessage) {
        scheduleState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        snackbarHost   = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent
    ) { padding ->
        AppBackground {
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {

                // Cabecera con logo centrado y botón atrás
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 56.dp)
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

                Row(
                    modifier              = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text  = "${pet?.name ?: "Furry"} Schedule",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Button(
                        onClick = { navController.navigate(Routes.addEvent(petId = petId)) },
                        colors  = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
                    ) {
                        Text("+ Event", color = Color.White)
                    }
                }

                // Pestañas: Próximos / Historial
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor   = Color.Transparent,
                    contentColor     = Color(0xFF1B5E20)
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick  = { selectedTab = 0 },
                        text     = { Text("Upcoming") }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick  = { selectedTab = 1 },
                        text     = { Text("History") }
                    )
                }

                when {
                    // Estado: cargando
                    scheduleState.isLoading -> {
                        Box(
                            modifier         = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Color(0xFF1B5E20))
                        }
                    }

                    // Estado: error de red
                    scheduleState.errorMessage != null -> {
                        Box(
                            modifier         = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text  = scheduleState.errorMessage!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    // Estado: con datos según la pestaña
                    else -> {
                        val events = if (selectedTab == 0) scheduleState.upcomingEvents
                        else scheduleState.pastEvents
                        val emptyMessage = if (selectedTab == 0) "No upcoming events"
                        else "No past events"
                        // En historial los eventos son solo de consulta (sin editar/eliminar)
                        val isHistory = selectedTab == 1

                        if (events.isEmpty()) {
                            Box(
                                modifier         = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(emptyMessage, color = Color.Gray)
                            }
                        } else {
                            LazyColumn(
                                modifier            = Modifier.fillMaxSize(),
                                contentPadding      = PaddingValues(horizontal = 24.dp, vertical = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(events) { event ->
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
                                                Text(
                                                    text  = "${event.date} · ${event.time}",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = Color.Gray
                                                )
                                                Text(
                                                    text  = event.title,
                                                    style = MaterialTheme.typography.titleMedium
                                                )
                                                if (!event.description.isNullOrBlank()) {
                                                    Text(
                                                        text  = event.description,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = Color.Gray
                                                    )
                                                }
                                            }
                                            // Iconos solo en próximos, no en historial
                                            if (!isHistory) {
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
                        }
                    }
                }
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
                // Recargamos el horario para reflejar el borrado
                eventViewModel.loadPetSchedule(petId)
            },
            onDismiss = { eventToDelete = null }
        )
    }
}