package com.example.happyfurries.ui.pet

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.happyfurries.R
import com.example.happyfurries.ui.AppBackground
import com.example.happyfurries.ui.viewmodel.PetViewModel

// Pantalla de detalle de una mascota.
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

    if (pet == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Cabecera con logo centrado, botón atrás a la izquierda
            // y círculo de mascota a la derecha
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 56.dp)
                    .height(56.dp)
            ) {
                // Botón atrás — izquierda
                IconButton(
                    onClick  = onBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }

                // Logo — centro exacto
                Image(
                    painter            = painterResource(id = R.drawable.logobig),
                    contentDescription = "Logo",
                    modifier           = Modifier
                        .size(56.dp)
                        .align(Alignment.Center)
                )

                // Círculo de mascota — derecha
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .border(3.dp, parseColorDetail(pet.colorHex), CircleShape)
                        .align(Alignment.CenterEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text     = pet.name,
                        fontSize = 12.sp,
                        color    = Color(0xFF1B5E20),
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text  = "Furry profile",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Name: ${pet.name}")
            Text("Species: ${pet.species}")
            if (!pet.breed.isNullOrBlank())     Text("Breed: ${pet.breed}")
            if (pet.weightKg > 0)               Text("Weight: ${pet.weightKg} kg")
            if (!pet.foodBrand.isNullOrBlank()) Text("Food brand: ${pet.foodBrand}")
            if (pet.dailyFoodGrams != null)     Text("Daily food: ${pet.dailyFoodGrams} g")
            if (!pet.notes.isNullOrBlank())     Text("Notes: ${pet.notes}")

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick  = onEdit,
                modifier = Modifier.fillMaxWidth(),
                colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
            ) {
                Text("Edit", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

fun parseColorDetail(hex: String): Color {
    return try {
        Color(android.graphics.Color.parseColor(hex))
    } catch (e: Exception) {
        Color(0xFF4CAF50)
    }
}