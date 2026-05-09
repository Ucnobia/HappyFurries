package com.example.happyfurries.ui.pet

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.happyfurries.data.entities.PetEntity
import com.example.happyfurries.ui.viewmodel.PetViewModel

@Composable
fun PetFormScreen(
    viewModel: PetViewModel,
    onPetSaved: () -> Unit
) {
    val name = remember { mutableStateOf("") }
    val birthDate = remember { mutableStateOf("") }
    val size = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Create your furry profile")

        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Name") }
        )

        OutlinedTextField(
            value = birthDate.value,
            onValueChange = { birthDate.value = it },
            label = { Text("Birth date") }
        )

        OutlinedTextField(
            value = size.value,
            onValueChange = { size.value = it },
            label = { Text("Size") }
        )

        Button(
            onClick = {
                val pet = PetEntity(
                    id = 0,
                    name = name.value,
                    colorHex = "#FFB6C1",
                    species = "",
                    breed = null,
                    weightKg = 0f,
                    foodBrand = null,
                    foodBagWeightKg = null,
                    dailyFoodGrams = null,
                    notes = null
                )

                viewModel.addPet(pet) {
                    onPetSaved()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}
