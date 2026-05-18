package com.example.happyfurries.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Tema de Happy Furies — solo modo claro, sin dynamic color
// Dynamic color lo desactivo porque quiero que siempre se vean
// mis colores personalizados independientemente del móvil

private val HappyFurriesColorScheme = lightColorScheme(
    primary          = GreenDark,        // botones principales, elementos seleccionados
    onPrimary        = White,            // texto sobre el color primary
    secondary        = GreenMedium,      // acentos secundarios
    onSecondary      = White,
    background       = BackgroundGrey,   // fondo general
    onBackground     = TextDark,         // texto sobre el fondo
    surface          = White,            // tarjetas y superficies
    onSurface        = TextDark,
    error            = Color(0xFFB00020),
    onError          = White
)

@Composable
fun HappyFurriesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = HappyFurriesColorScheme,
        typography  = Typography,
        content     = content
    )
}