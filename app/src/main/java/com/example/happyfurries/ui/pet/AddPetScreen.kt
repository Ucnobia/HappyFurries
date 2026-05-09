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
fun AddPetScreen(
    viewModel: PetViewModel,
    onPetSaved: () -> Unit = {}
) {
    val name = remember { mutableStateOf("") }
    val species = remember { mutableStateOf("") }
    val colorHex = remember { mutableStateOf("#FFB6C1") } // temporal

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Add Pet")

        OutlinedTextField(
            value = name.value,
            onValueChange = { name.value = it },
            label = { Text("Name") }
        )

        OutlinedTextField(
            value = species.value,
            onValueChange = { species.value = it },
            label = { Text("Species") }
        )

        Button(
            onClick = {
                val pet = PetEntity(
                    id = 0,
                    name = name.value,
                    colorHex = colorHex.value,
                    species = species.value,
                    weightKg = 0f,
                    breed = null,
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
            Text("Save")
        }
    }
}
