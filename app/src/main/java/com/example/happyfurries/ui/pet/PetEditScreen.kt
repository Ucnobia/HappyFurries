package com.example.happyfurries.ui.pet

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.happyfurries.R
import com.example.happyfurries.data.entities.PetEntity
import com.example.happyfurries.ui.AppBackground
import com.example.happyfurries.ui.viewmodel.PetViewModel

// Pantalla para editar una mascota existente.
// Carga los datos actuales en los campos para que el usuario
// solo cambie lo que quiera.

@Composable
fun PetEditScreen(
    petId: Int,
    petViewModel: PetViewModel,
    onSaved: () -> Unit,
    onBack: () -> Unit
) {
    val pets = petViewModel.pets.collectAsState().value
    val pet  = pets.find { it.id == petId }

    if (pet == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val name      = remember { mutableStateOf(pet.name) }
    val species   = remember { mutableStateOf(pet.species) }
    val breed     = remember { mutableStateOf(pet.breed ?: "") }
    val weightKg  = remember { mutableStateOf(if (pet.weightKg > 0) pet.weightKg.toString() else "") }
    val foodBrand = remember { mutableStateOf(pet.foodBrand ?: "") }
    val dailyFood = remember { mutableStateOf(pet.dailyFoodGrams?.toString() ?: "") }
    val notes     = remember { mutableStateOf(pet.notes ?: "") }
    // Color blanco roto para los campos — consistente con el fondo de la app
    val fieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = Color(0xFFF5F5F5),
        focusedContainerColor   = Color(0xFFF5F5F5)
    )
    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(58.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Cabecera con logo centrado y botón atrás a la izquierda
            Box(
                modifier = Modifier
                    .fillMaxWidth()
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

            Text(
                text  = "Edit ${pet.name}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value         = name.value,
                onValueChange = { name.value = it },
                label         = { Text("Name *") },
                modifier      = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            OutlinedTextField(
                value         = species.value,
                onValueChange = { species.value = it },
                label         = { Text("Species * (Dog, Cat, Rabbit...)") },
                modifier      = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            OutlinedTextField(
                value         = breed.value,
                onValueChange = { breed.value = it },
                label         = { Text("Breed") },
                modifier      = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            OutlinedTextField(
                value         = weightKg.value,
                onValueChange = { weightKg.value = it },
                label         = { Text("Weight (kg)") },
                modifier      = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            OutlinedTextField(
                value         = foodBrand.value,
                onValueChange = { foodBrand.value = it },
                label         = { Text("Food brand") },
                modifier      = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            OutlinedTextField(
                value         = dailyFood.value,
                onValueChange = { dailyFood.value = it },
                label         = { Text("Daily food (grams)") },
                modifier      = Modifier.fillMaxWidth(),
                colors = fieldColors
            )

            OutlinedTextField(
                value         = notes.value,
                onValueChange = { notes.value = it },
                label         = { Text("Notes") },
                modifier      = Modifier.fillMaxWidth(),
                minLines      = 2,
                colors = fieldColors
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (name.value.isBlank() || species.value.isBlank()) return@Button

                    val updatedPet = PetEntity(
                        id              = pet.id,
                        name            = name.value.trim(),
                        species         = species.value.trim(),
                        breed           = breed.value.trim().ifBlank { null },
                        colorHex        = pet.colorHex,
                        weightKg        = weightKg.value.toFloatOrNull() ?: 0f,
                        foodBrand       = foodBrand.value.trim().ifBlank { null },
                        foodBagWeightKg = pet.foodBagWeightKg,
                        dailyFoodGrams  = dailyFood.value.toIntOrNull(),
                        notes           = notes.value.trim().ifBlank { null }
                    )

                    petViewModel.updatePet(updatedPet) { onSaved() }
                },
                modifier = Modifier.fillMaxWidth(),
                colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
            ) {
                Text("Save changes", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}