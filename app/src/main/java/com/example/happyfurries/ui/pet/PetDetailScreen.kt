package com.example.happyfurries.ui.pet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.happyfurries.ui.viewmodel.PetViewModel

// Pantalla de detalle de una mascota.
// Recibe el ID, busca la mascota en el ViewModel y muestra su info.
// De momento solo permite editar — el borrado es una decisión de diseño
// pendiente porque eliminar una mascota es un momento delicado para el usuario.

@Composable
fun PetDetailScreen(
    petId: Int,
    petViewModel: PetViewModel,
    onEdit: () -> Unit,
    onBack: () -> Unit
) {
    val pets = petViewModel.pets.collectAsState().value
    val pet  = pets.find { it.id == petId }

    // Mientras no tengamos la mascota mostramos un loader
    if (pet == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEEEEE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Botón de volver atrás
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }

            Text(
                text = "Furry profile",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Información de la mascota
            Text("Name: ${pet.name}")
            Text("Species: ${pet.species}")
            if (!pet.breed.isNullOrBlank())     Text("Breed: ${pet.breed}")
            if (pet.weightKg > 0)               Text("Weight: ${pet.weightKg} kg")
            if (!pet.foodBrand.isNullOrBlank()) Text("Food brand: ${pet.foodBrand}")
            if (pet.dailyFoodGrams != null)     Text("Daily food: ${pet.dailyFoodGrams} g")
            if (!pet.notes.isNullOrBlank())     Text("Notes: ${pet.notes}")

            Spacer(modifier = Modifier.weight(1f))

            // Botón editar
            Button(
                onClick = onEdit,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1B5E20)
                )
            ) {
                Text("Edit", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}