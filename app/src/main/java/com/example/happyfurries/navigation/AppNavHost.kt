package com.example.happyfurries.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.happyfurries.ui.petform.PetFormScreen
import com.example.happyfurries.ui.splash.SplashScreen
import com.example.happyfurries.ui.welcome.WelcomeScreen

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen {
                navController.navigate("welcome") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }

        composable("welcome") {
            WelcomeScreen {
                navController.navigate("pet_form")
                //Ruta a pantalla de formulario

            }
        }

        composable("pet_form") {
            PetFormScreen {
                navController.navigate("main_calendar")
            }
        }
    }
}


