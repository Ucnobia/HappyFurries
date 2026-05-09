package com.example.happyfurries.ui.pet

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.happyfurries.ui.viewmodel.PetViewModel

@Composable
fun PetDetailScreen(
    petId: Int,
    petViewModel: PetViewModel
) {
    val pets = petViewModel.pets.collectAsState().value
    val pet = pets.find { it.id == petId }

    if (pet == null) {
        // Si aún no está cargado
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading pet...")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Pet Profile",
            style = MaterialTheme.typography.headlineMedium
        )

        Text("Name: ${pet.name}")
        Text("Initial: ${pet.initial}")
        Text("Species: ${pet.species}")
        Text("Color: ${pet.colorHex}")
        Text("Weight: ${pet.weightKg} kg")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                // Aquí luego añadimos editar
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Edit")
        }

        Button(
            onClick = {
                // Aquí luego añadimos eliminar
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Delete")
        }
    }
}
