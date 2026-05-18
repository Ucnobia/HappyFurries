package com.example.happyfurries.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.happyfurries.R
import com.example.happyfurries.data.entities.PetEntity
import com.example.happyfurries.navigation.Routes
import com.example.happyfurries.ui.calendar.Calendar
import com.example.happyfurries.ui.viewmodel.EventViewModel
import com.example.happyfurries.ui.viewmodel.PetViewModel

@Composable
fun MainScreen(
    navController: NavController,
    petViewModel: PetViewModel,
    eventViewModel: EventViewModel
) {
    // Cargo las mascotas reales del ViewModel al abrir la pantalla
    LaunchedEffect(Unit) {
        petViewModel.loadPets()
    }

    val events = eventViewModel.events.collectAsState().value
    val pets   = petViewModel.pets.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AppLogoHeader()

        Spacer(modifier = Modifier.height(16.dp))

        // Paso el eventViewModel al calendario para que muestre eventos reales
        Calendar(eventViewModel = eventViewModel)

        Spacer(modifier = Modifier.height(24.dp))

        UpcomingEventsSection(events)

        Spacer(modifier = Modifier.height(24.dp))

        // Paso las mascotas reales y navego al detalle al hacer click
        PetRow(
            pets = pets,
            onPetClick = { pet ->
                navController.navigate(Routes.petDetail(pet.id))
            },
            onAddPetClick = {
                navController.navigate(Routes.ADD_PET)
            }
        )
    }
}

@Composable
fun AppLogoHeader() {
    Image(
        painter = painterResource(id = R.drawable.logobig),
        contentDescription = "Logo",
        modifier = Modifier.size(40.dp)
    )
}

@Composable
fun UpcomingEventsSection(events: List<com.example.happyfurries.data.entities.EventEntity>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Upcoming events",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (events.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color(0xFFF0F0F0)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No events for today", color = Color.Gray)
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                events.forEach { event ->
                    Text("• ${event.title} — ${event.time}")
                }
            }
        }
    }
}

// PetRow ahora recibe PetEntity en lugar de String
@Composable
fun PetRow(
    pets: List<PetEntity>,
    onPetClick: (PetEntity) -> Unit,
    onAddPetClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        pets.forEach { pet ->
            PetCircle(
                letter = pet.name,
                color  = parseColor(pet.colorHex),
                onClick = { onPetClick(pet) }
            )
        }

        AddPetCircle(onClick = onAddPetClick)
    }
}

// Cada círculo muestra la inicial de la mascota con su color personalizado
@Composable
fun PetCircle(letter: String, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .border(3.dp, color, CircleShape)
            .background(Color.White)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = letter, style = MaterialTheme.typography.titleMedium)
    }
}

@Composable
fun AddPetCircle(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(Color(0xFFD0D0D0))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = "+", style = MaterialTheme.typography.titleMedium)
    }
}

// Convierte el string hexadecimal del colorHex a un Color de Compose
// Si el formato es incorrecto devuelvo verde por defecto
fun parseColor(hex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (e: Exception) {
        Color(0xFF4CAF50)
    }
}