package com.example.happyfurries.ui.event

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
import com.example.happyfurries.data.entities.EventEntity
import com.example.happyfurries.ui.AppBackground
import com.example.happyfurries.ui.viewmodel.EventViewModel
import com.example.happyfurries.ui.viewmodel.PetViewModel
import java.time.LocalDate

// Pantalla para crear un nuevo evento.
// Recibe una fecha opcional (si se abre desde el calendario)
// y un petId opcional (si se abre desde el perfil de una mascota).

@Composable
fun AddEventScreen(
    eventViewModel: EventViewModel,
    petViewModel: PetViewModel,
    initialDate: String? = null,
    initialPetId: Int? = null,
    onSaved: () -> Unit,
    onBack: () -> Unit
) {
    var title       by remember { mutableStateOf("") }
    var date        by remember { mutableStateOf(initialDate ?: "") }
    var time        by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedPetId by remember { mutableStateOf(initialPetId) }
    var expanded    by remember { mutableStateOf(false) }
    var showFormatError by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        petViewModel.loadPets()
    }

    val pets = petViewModel.pets.collectAsState().value

    val fieldColors = OutlinedTextFieldDefaults.colors(
        unfocusedContainerColor = Color(0xFFF5F5F5),
        focusedContainerColor   = Color(0xFFF5F5F5)
    )

    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 56.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(8.dp))

            // Cabecera con logo centrado y botón atrás
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 56.dp)
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
                text  = "Add a new event",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Título — obligatorio
            OutlinedTextField(
                value         = title,
                onValueChange = { title = it },
                label         = { Text("Title *") },
                modifier      = Modifier.fillMaxWidth(),
                colors        = fieldColors
            )

            // Fecha — obligatoria, formato YYYY-MM-DD
            OutlinedTextField(
                value         = date,
                onValueChange = { date = it },
                label         = { Text("Date * (YYYY-MM-DD)") },
                modifier      = Modifier.fillMaxWidth(),
                colors        = fieldColors
            )

            // Hora — obligatoria, formato HH:mm
            OutlinedTextField(
                value         = time,
                onValueChange = { time = it },
                label         = { Text("Time * (HH:mm)") },
                modifier      = Modifier.fillMaxWidth(),
                colors        = fieldColors
            )

            // Descripción — opcional
            OutlinedTextField(
                value         = description,
                onValueChange = { description = it },
                label         = { Text("Description (optional)") },
                modifier      = Modifier.fillMaxWidth(),
                minLines      = 2,
                colors        = fieldColors
            )

            // Selector de mascota — opcional
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value         = pets.find { it.id == selectedPetId }?.name ?: "No furry selected",
                    onValueChange = {},
                    readOnly      = true,
                    label         = { Text("Furry (optional)") },
                    modifier      = Modifier.fillMaxWidth(),
                    colors        = fieldColors
                )
                // Botón invisible encima para abrir el dropdown
                Surface(
                    modifier = Modifier
                        .matchParentSize(),
                    color    = Color.Transparent,
                    onClick  = { expanded = true }
                ) {}
            }

            // Dropdown de mascotas
            DropdownMenu(
                expanded         = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text    = { Text("No furry") },
                    onClick = { selectedPetId = null; expanded = false }
                )
                pets.forEach { pet ->
                    DropdownMenuItem(
                        text    = { Text(pet.name) },
                        onClick = { selectedPetId = pet.id; expanded = false }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Validaciones de formato
            val dateRegex = Regex("""^\d{4}-\d{2}-\d{2}$""")
            val timeRegex = Regex("""^\d{2}:\d{2}$""")

            Button(
                onClick = {
                    if (title.isBlank() || date.isBlank() || time.isBlank()) return@Button
                    // Validar formato antes de guardar
                    if (!dateRegex.matches(date.trim()) || !timeRegex.matches(time.trim())) {
                        showFormatError = true
                        return@Button
                    }
                    // Validar que la fecha no esté en el pasado
                    val parsedDate = try { LocalDate.parse(date.trim()) } catch (e: Exception) { null }
                    if (parsedDate == null || parsedDate.isBefore(LocalDate.now())) {
                        showFormatError = true
                        return@Button
                    }

                    val newEvent = EventEntity(
                        id          = 0,
                        petId       = selectedPetId ?: 0,
                        title       = title.trim(),
                        date        = date.trim(),
                        time        = time.trim(),
                        description = description.trim().ifBlank { null }
                    )
                    eventViewModel.addEvent(newEvent) { onSaved() }
                },
                modifier = Modifier.fillMaxWidth(),
                colors   = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
            ) {
                Text("Save event", color = Color.White)
            }

            // Mensaje de error de validación
            if (showFormatError) {
                Text(
                    text  = "Check the date (YYYY-MM-DD, not in the past) and time (HH:mm)",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}