package com.example.happyfurries.ui.pet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.happyfurries.ui.viewmodel.EventViewModel

@Composable
fun PetScheduleScreen(
    eventViewModel: EventViewModel
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Pet Schedule Screen")
    }
}
