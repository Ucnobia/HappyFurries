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
import com.example.happyfurries.navigation.Routes
import com.example.happyfurries.ui.AppBackground
import com.example.happyfurries.ui.viewmodel.EventViewModel
import com.example.happyfurries.ui.viewmodel.PetViewModel

// Pantalla que muestra todos los eventos de una mascota concreta.
// El título muestra el nombre de la mascota para que el usuario sepa dónde está.

@Composable
fun PetScheduleScreen(
    petId: Int,
    petViewModel: PetViewModel,
    eventViewModel: EventViewModel,
    navController: NavController,
    onBack: () -> Unit
) {
    LaunchedEffect(petId) {
        eventViewModel.loadEventsByPet(petId)
        petViewModel.loadPets()
    }

    val events = eventViewModel.events.collectAsState().value
    val pets   = petViewModel.pets.collectAsState().value
    val pet    = pets.find { it.id == petId }

    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {

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
                // Título con el nombre de la mascota
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

            if (events.isEmpty()) {
                Box(
                    modifier         = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No events yet for this furry", color = Color.Gray)
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
                            Column(modifier = Modifier.padding(16.dp)) {
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
                            // Icono editar
                            IconButton(
                                onClick = { navController.navigate(Routes.editEvent(event.id)) }
                            ) {
                                Icon(
                                    imageVector        = Icons.Outlined.Edit,
                                    contentDescription = "Edit event",
                                    tint               = Color(0xFF1B5E20)
                                )
                            }
                            // Icono eliminar
                            IconButton(
                                onClick = { eventViewModel.deleteEvent(event) }
                            ) {
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