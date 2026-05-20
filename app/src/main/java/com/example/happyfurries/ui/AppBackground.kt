package com.example.happyfurries.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

// Fondo reutilizable de Happy Furies.
// Forma de nube gris sobre fondo naranja.
// Consiste en un rectángulo superior + dos círculos grandes solapados abajo.

@Composable
fun AppBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFCC80))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val grey = Color(0xFFDDDDDD)

            // Rectángulo que cubre toda la parte superior
            drawRect(
                color   = grey,
                topLeft = Offset(0f, 0f),
                size    = Size(w, h * 0.55f)
            )

            // Círculo izquierdo — lóbulo izquierdo de la nube
            drawCircle(
                color  = grey,
                radius = w * 0.42f,
                center = Offset(w * 0.08f, h * 0.22f)
            )

            // Círculo medio - mas pequeño
            drawCircle(
                color  = grey,
                radius = w * 0.32f,
                center = Offset(w * 0.32f, h * 0.48f)
            )

            // Círculo derecho — lóbulo derecho de la nube
            drawCircle(
                color  = grey,
                radius = w * 0.48f,
                center = Offset(w * 0.86f, h * 0.50f)
            )
        }

        content()
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun AppBackgroundPreview() {
    AppBackground { }
}