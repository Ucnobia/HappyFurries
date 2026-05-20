package com.example.happyfurries.ui.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.happyfurries.R
import com.example.happyfurries.ui.AppBackground

@Composable
fun WelcomeScreen(onStartClick: () -> Unit) {
    AppBackground {
        Box(modifier = Modifier.fillMaxSize()) {

            // Logo centrado en la parte gris
            Image(
                painter = painterResource(id = R.drawable.logobig),
                contentDescription = "Happy Furries logo",
                modifier = Modifier
                    .size(410.dp)
                    .align(Alignment.Center)
            )

            // Botón en la parte naranja abajo
            Button(
                onClick = onStartClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp)
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 120.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF1B5E20)
                )
            ) {
                Text("Get started")
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(onStartClick = {})
}