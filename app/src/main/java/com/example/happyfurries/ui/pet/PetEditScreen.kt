package com.example.happyfurries.ui.pet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.happyfurries.data.entities.PetEntity
import com.example.happyfurries.ui.viewmodel.PetViewModel

// Pantalla para editar una mascota existente.
// Funciona igual que PetFormScreen pero carga los datos actuales
// de la mascota en los campos para que el usuario solo cambie lo que quiera.

@Composable
fun PetEditScreen(
    petId: Int,
    petViewModel: PetViewModel,
    onSaved: () -> Unit,
    onBack: () -> Unit
) {
    val pets = petViewModel.pets.collectAsState().value
    val pet  = pets.find { it.id == petId }

    // Mientras carga la mascota mostramos un loader
    if (pet == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Inicializo cada campo con el valor actual de la mascota
    // Si el usuario no toca un campo, se queda como estaba
    val name      = remember { mutableStateOf(pet.name) }
    val species   = remember { mutableStateOf(pet.species) }
    val breed     = remember { mutableStateOf(pet.breed ?: "") }
    val weightKg  = remember { mutableStateOf(if (pet.weightKg > 0) pet.weightKg.toString() else "") }
    val foodBrand = remember { mutableStateOf(pet.foodBrand ?: "") }
    val dailyFood = remember { mutableStateOf(pet.dailyFoodGrams?.toString() ?: "") }
    val notes     = remember { mutableStateOf(pet.notes ?: "") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEEEEEE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Botón de volver sin guardar
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }

            Text(
                text = "Edit ${pet.name}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text("Name *") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = species.value,
                onValueChange = { species.value = it },
                label = { Text("Species * (Dog, Cat, Rabbit...)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = breed.value,
                onValueChange = { breed.value = it },
                label = { Text("Breed") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = weightKg.value,
                onValueChange = { weightKg.value = it },
                label = { Text("Weight (kg)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = foodBrand.value,
                onValueChange = { foodBrand.value = it },
                label = { Text("Food brand") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = dailyFood.value,
                onValueChange = { dailyFood.value = it },
                label = { Text("Daily food (grams)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = notes.value,
                onValueChange = { notes.value = it },
                label = { Text("Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // Solo guardo si nombre y especie tienen valor
                    if (name.value.isBlank() || species.value.isBlank()) return@Button

                    // Construyo la mascota actualizada manteniendo el mismo ID y colorHex
                    val updatedPet = PetEntity(
                        id             = pet.id,
                        name           = name.value.trim(),
                        species        = species.value.trim(),
                        breed          = breed.value.trim().ifBlank { null },
                        colorHex       = pet.colorHex,
                        weightKg       = weightKg.value.toFloatOrNull() ?: 0f,
                        foodBrand      = foodBrand.value.trim().ifBlank { null },
                        foodBagWeightKg = pet.foodBagWeightKg,
                        dailyFoodGrams = dailyFood.value.toIntOrNull(),
                        notes          = notes.value.trim().ifBlank { null }
                    )

                    petViewModel.updatePet(updatedPet) { onSaved() }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1B5E20)
                )
            ) {
                Text("Save changes", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}