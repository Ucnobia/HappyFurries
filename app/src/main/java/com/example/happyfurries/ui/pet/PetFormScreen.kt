package com.example.happyfurries.ui.pet

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
// Pantalla INICIAL formulario mascota
@Composable
fun PetFormScreen(
    onSubmitClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Create your furry profile")

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Name") }
        )

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Birth date") }
        )

        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Size") }
        )

        Button(
            onClick = onSubmitClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Submit")
        }
    }
}
