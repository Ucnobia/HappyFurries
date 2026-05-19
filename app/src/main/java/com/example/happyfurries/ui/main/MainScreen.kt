package com.example.happyfurries.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.happyfurries.R
import com.example.happyfurries.data.entities.PetEntity
import com.example.happyfurries.navigation.Routes
import com.example.happyfurries.ui.AppBackground
import com.example.happyfurries.ui.calendar.Calendar
import com.example.happyfurries.ui.viewmodel.EventViewModel
import com.example.happyfurries.ui.viewmodel.PetViewModel

@Composable
fun MainScreen(
    navController: NavController,
    petViewModel: PetViewModel,
    eventViewModel: EventViewModel
) {
    LaunchedEffect(Unit) {
        petViewModel.loadPets()
    }

    val events = eventViewModel.events.collectAsState().value
    val pets   = petViewModel.pets.collectAsState().value

    AppBackground {
        Column(modifier = Modifier.fillMaxSize()) {

            // Logo pequeño centrado arriba en la parte gris
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 58.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logobig),
                    contentDescription = "Happy Furries logo",
                    modifier = Modifier.size(56.dp)
                )
            }

            // Calendario
            Calendar(eventViewModel = eventViewModel)

            Spacer(modifier = Modifier.height(8.dp))

            // Tarjetas de eventos del día
            UpcomingEventsSection(events)

            Spacer(modifier = Modifier.weight(1f))

            // Barra inferior verde con las mascotas
            PetBottomBar(
                pets            = pets,
                onPetClick      = { pet -> navController.navigate(Routes.petDetail(pet.id)) },
                onEditClick     = { pet -> navController.navigate(Routes.petEdit(pet.id)) },
                onScheduleClick = { pet -> navController.navigate(Routes.petSchedule(pet.id)) },
                onAddPetClick   = { navController.navigate(Routes.ADD_PET) }
            )
        }
    }
}

@Composable
fun UpcomingEventsSection(
    events: List<com.example.happyfurries.data.entities.EventEntity>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (events.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No events for today", color = Color.Gray)
                }
            }
        } else {
            events.forEach { event ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(Color(0xFF4CAF50), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text  = event.time,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                            Text(
                                text     = event.title,
                                style    = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            if (!event.description.isNullOrBlank()) {
                                Text(
                                    text     = event.description,
                                    style    = MaterialTheme.typography.bodySmall,
                                    color    = Color.Gray,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PetBottomBar(
    pets: List<PetEntity>,
    onPetClick: (PetEntity) -> Unit,
    onEditClick: (PetEntity) -> Unit,
    onScheduleClick: (PetEntity) -> Unit,
    onAddPetClick: () -> Unit
) {
    // Margen abajo para que se vea el fondo naranja debajo de la barra
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 45.dp)
            .background(Color(0xFF1B5E20))
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            pets.forEach { pet ->
                PetBottomItem(
                    pet             = pet,
                    onPetClick      = { onPetClick(pet) },
                    onEditClick     = { onEditClick(pet) },
                    onScheduleClick = { onScheduleClick(pet) }
                )
            }

            // Botón añadir mascota
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(5.dp, Color(0xFFFFCC80), CircleShape)
                        .background(Color.White)
                        .clickable { onAddPetClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector        = Icons.Default.Add,
                        contentDescription = "Add pet",
                        tint               = Color(0xFF1B5E20),
                        modifier           = Modifier.size(28.dp)
                    )
                }
                //Espacio del mismo tamaño que los botones de las mascotas
                //Para que quede alineado
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun PetBottomItem(
    pet: PetEntity,
    onPetClick: () -> Unit,
    onEditClick: () -> Unit,
    onScheduleClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Círculo blanco con el nombre de la mascota y borde de su color
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(5.dp, parseColor(pet.colorHex), CircleShape)
                .background(Color.White)
                .clickable { onPetClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text     = pet.name,
                color    = Color(0xFF1B5E20),
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(4.dp)
            )
        }

        // Tres iconos debajo del círculo
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(
                imageVector        = Icons.Default.Edit,
                contentDescription = "Edit pet",
                tint               = Color.White,
                modifier           = Modifier
                    .size(20.dp)
                    .clickable { onEditClick() }
            )
            Icon(
                imageVector        = Icons.Default.Notifications,
                contentDescription = "Pet schedule",
                tint               = Color.White,
                modifier           = Modifier
                    .size(20.dp)
                    .clickable { onScheduleClick() }
            )
            Icon(
                imageVector        = Icons.Default.Info,
                contentDescription = "Pet detail",
                tint               = Color.White,
                modifier           = Modifier
                    .size(20.dp)
                    .clickable { onPetClick() }
            )
        }
    }
}

fun parseColor(hex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (e: Exception) {
        Color(0xFF4CAF50)
    }
}