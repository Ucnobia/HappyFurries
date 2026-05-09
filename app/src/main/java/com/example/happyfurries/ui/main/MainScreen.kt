package com.example.happyfurries.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.happyfurries.R
import com.example.happyfurries.navigation.Routes
import com.example.happyfurries.ui.calendar.Calendar
import com.example.happyfurries.ui.viewmodel.PetViewModel
import com.example.happyfurries.ui.viewmodel.EventViewModel

@Composable
fun MainScreen(
    navController: NavController,
    petViewModel: PetViewModel,
    eventViewModel: EventViewModel
) {
    val events = eventViewModel.events.collectAsState().value
    val pets = listOf("Misha", "Luna", "Toby") // Temporal

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AppLogoHeader()

        Spacer(modifier = Modifier.height(16.dp))

        Calendar()

        Spacer(modifier = Modifier.height(24.dp))

        UpcomingEventsSection(events)

        Spacer(modifier = Modifier.height(24.dp))

        PetRow(
            pets = pets,
            onPetClick = { petName ->
                navController.navigate(Routes.ADD_PET)
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
            text = "Próximos Eventos",
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
                Text(
                    text = "No hay eventos",
                    color = Color.Gray
                )
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

@Composable
fun PetRow(
    pets: List<String>,
    onPetClick: (String) -> Unit,
    onAddPetClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        pets.forEach { petName ->
            PetCircle(
                letter = petName.first().uppercase(),
                onClick = { onPetClick(petName) }
            )
        }

        AddPetCircle(onClick = onAddPetClick)
    }
}

@Composable
fun PetCircle(letter: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(Color(0xFFE0E0E0))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            style = MaterialTheme.typography.titleMedium
        )
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
        Text(
            text = "+",
            style = MaterialTheme.typography.titleMedium
        )
    }
}
