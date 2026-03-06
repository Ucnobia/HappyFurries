package com.example.happyfurries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.happyfurries.navigation.AppNavHost
import com.example.happyfurries.ui.theme.HappyFurriesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappyFurriesTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}
