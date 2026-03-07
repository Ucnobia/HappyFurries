package com.example.happyfurries.ui.calendarscreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.happyfurries.navigation.Routes

@Composable
fun CalendarScreen(navController: NavController) {
    Column {
        Text("Calendar Screen")

        Button(onClick = { navController.navigate(Routes.ADD_PET) }) {
            Text("Añadir nueva mascota")
        }
    }
}





