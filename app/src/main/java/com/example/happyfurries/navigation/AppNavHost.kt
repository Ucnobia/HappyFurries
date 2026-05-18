package com.example.happyfurries.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.happyfurries.ui.viewmodel.PetViewModel
import com.example.happyfurries.ui.viewmodel.EventViewModel
import com.example.happyfurries.ui.pet.AddPetScreen
import com.example.happyfurries.ui.pet.PetDetailScreen
import com.example.happyfurries.ui.pet.PetEditScreen
import com.example.happyfurries.ui.pet.PetFormScreen
import com.example.happyfurries.ui.pet.PetScheduleScreen
import com.example.happyfurries.ui.splash.SplashScreen
import com.example.happyfurries.ui.welcome.WelcomeScreen
import com.example.happyfurries.ui.main.MainScreen

// Aquí defino toda la navegación de la app.
// Cada composable() es una pantalla con su ruta.
// NavHost sabe a qué pantalla ir según la ruta que le paso.

@Composable
fun AppNavHost(
    navController: NavHostController,
    petViewModel: PetViewModel,
    eventViewModel: EventViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        // Splash — pantalla de inicio, redirige al welcome automáticamente
        composable(Routes.SPLASH) {
            SplashScreen {
                navController.navigate(Routes.WELCOME) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
        }

        // Welcome — pantalla de bienvenida con el botón "Get started"
        composable(Routes.WELCOME) {
            WelcomeScreen {
                navController.navigate(Routes.PET_FORM)
            }
        }

        // Formulario para crear la primera mascota
        composable(Routes.PET_FORM) {
            PetFormScreen(
                viewModel = petViewModel,
                onPetSaved = {
                    navController.navigate(Routes.MAIN_SCREEN)
                }
            )
        }

        // Añadir mascota adicional desde la pantalla principal
        composable(Routes.ADD_PET) {
            AddPetScreen(
                viewModel = petViewModel,
                onPetSaved = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla principal con el calendario y las mascotas
        composable(Routes.MAIN_SCREEN) {
            MainScreen(
                navController = navController,
                petViewModel = petViewModel,
                eventViewModel = eventViewModel
            )
        }

        // Detalle de una mascota — muestra su info y botón de editar
        composable(Routes.PET_DETAIL) { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId")?.toInt() ?: 0
            PetDetailScreen(
                petId = petId,
                petViewModel = petViewModel,
                onEdit = { navController.navigate(Routes.petEdit(petId)) },
                onBack = { navController.popBackStack() }
            )
        }

        // Editar una mascota existente — carga sus datos actuales en el formulario
        composable(Routes.PET_EDIT) { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId")?.toInt() ?: 0
            PetEditScreen(
                petId = petId,
                petViewModel = petViewModel,
                onSaved = { navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }

        // Agenda de una mascota — muestra sus eventos
        composable(Routes.PET_SCHEDULE) { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId")?.toInt() ?: 0
            PetScheduleScreen(
                petId = petId,
                eventViewModel = eventViewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}