package com.example.happyfurries.ui.pet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

// Primera pantalla de creación de mascota — sin botón atrás
// porque es el onboarding inicial de la app.

@Composable
fun PetFormScreen(
    viewModel: PetViewModel,
    onPetSaved: () -> Unit
) {
    val name      = remember { mutableStateOf("") }
    val species   = remember { mutableStateOf("") }
    val breed     = remember { mutableStateOf("") }
    val weightKg  = remember { mutableStateOf("") }
    val foodBrand = remember { mutableStateOf("") }
    val dailyFood = remember { mutableStateOf("") }
    val notes     = remember { mutableStateOf("") }
    val colorHex  = remember { mutableStateOf("#4CAF50") }

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
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(58.dp))

            Image(
                painter            = painterResource(id = R.drawable.logobig),
                contentDescription = "Happy Furries logo",
                modifier           = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text  = "Let's get started, tell us about your furry!",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value         = name.value,
                onValueChange = { name.value = it },
                label         = { Text("Name *") },
                modifier      = Modifier.fillMaxWidth(),
                colors        = fieldColors
            )
            OutlinedTextField(
                value         = species.value,
                onValueChange = { species.value = it },
                label         = { Text("Species * (Dog, Cat, Rabbit...)") },
                modifier      = Modifier.fillMaxWidth(),
                colors        = fieldColors
            )
            OutlinedTextField(
                value         = breed.value,
                onValueChange = { breed.value = it },
                label         = { Text("Breed") },
                modifier      = Modifier.fillMaxWidth(),
                colors        = fieldColors
            )
            OutlinedTextField(
                value         = weightKg.value,
                onValueChange = { weightKg.value = it },
                label         = { Text("Weight (kg)") },
                modifier      = Modifier.fillMaxWidth(),
                colors        = fieldColors
            )
            OutlinedTextField(
                value         = foodBrand.value,
                onValueChange = { foodBrand.value = it },
                label         = { Text("Food brand") },
                modifier      = Modifier.fillMaxWidth(),
                colors        = fieldColors
            )
            OutlinedTextField(
                value         = dailyFood.value,
                onValueChange = { dailyFood.value = it },
                label         = { Text("Daily food (grams)") },
                modifier      = Modifier.fillMaxWidth(),
                colors        = fieldColors
            )
            OutlinedTextField(
                value         = notes.value,
                onValueChange = { notes.value = it },
                label         = { Text("Notes") },
                modifier      = Modifier.fillMaxWidth(),
                minLines      = 2,
                colors        = fieldColors
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (name.value.isBlank() || species.value.isBlank()) return@Button
                    val pet = PetEntity(
                        id              = 0,
                        name            = name.value.trim(),
                        species         = species.value.trim(),
                        breed           = breed.value.trim().ifBlank { null },
                        colorHex        = colorHex.value,
                        weightKg        = weightKg.value.toFloatOrNull() ?: 0f,
                        foodBrand       = foodBrand.value.trim().ifBlank { null },
                        foodBagWeightKg = null,
                        dailyFoodGrams  = dailyFood.value.toIntOrNull(),
                        notes           = notes.value.trim().ifBlank { null }
                    )
                    viewModel.addPet(pet) { onPetSaved() }
                },
                modifier = Modifier.fillMaxWidth(),
                colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
            ) {
                Text("Save furry", color = Color.White)
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}