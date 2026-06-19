package com.example.happyfurries.ui.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun ConfirmDeleteDialog(
    title: String,
    text: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title            = { Text(title) },
        text             = { Text(text) },
        confirmButton    = {
            TextButton(onClick = onConfirm) {
                Text("Delete", color = Color(0xFFB00020))
            }
        },
        dismissButton    = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color(0xFF1B5E20))
            }
        }
    )
}