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

// Toda la navegación de la app.
// isFirstTime decide si después del splash vamos al welcome o directo al main.

@Composable
fun AppNavHost(
    navController: NavHostController,
    petViewModel: PetViewModel,
    eventViewModel: EventViewModel,
    isFirstTime: Boolean,
    onFirstTimeComplete: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        // Splash — decide a dónde ir según si es la primera vez
        composable(Routes.SPLASH) {
            SplashScreen {
                if (isFirstTime) {
                    navController.navigate(Routes.WELCOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                } else {
                    navController.navigate(Routes.MAIN_SCREEN) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            }
        }

        // Welcome — solo se ve la primera vez
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
                    // Marcamos que ya no es la primera vez
                    onFirstTimeComplete()
                    navController.navigate(Routes.MAIN_SCREEN) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }
            )
        }

        // Añadir mascota adicional desde la pantalla principal
        composable(Routes.ADD_PET) {
            AddPetScreen(
                viewModel  = petViewModel,
                onPetSaved = { navController.popBackStack() },
                onBack     = { navController.popBackStack() }
            )
        }

        // Pantalla principal
        composable(Routes.MAIN_SCREEN) {
            MainScreen(
                navController  = navController,
                petViewModel   = petViewModel,
                eventViewModel = eventViewModel
            )
        }

        // Detalle de mascota
        composable(Routes.PET_DETAIL) { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId")?.toInt() ?: 0
            PetDetailScreen(
                petId      = petId,
                petViewModel = petViewModel,
                onEdit     = { navController.navigate(Routes.petEdit(petId)) },
                onBack     = { navController.popBackStack() }
            )
        }

        // Editar mascota
        composable(Routes.PET_EDIT) { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId")?.toInt() ?: 0
            PetEditScreen(
                petId      = petId,
                petViewModel = petViewModel,
                onSaved    = { navController.popBackStack() },
                onBack     = { navController.popBackStack() }
            )
        }

        // Agenda de mascota
        composable(Routes.PET_SCHEDULE) { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId")?.toInt() ?: 0
            PetScheduleScreen(
                petId          = petId,
                eventViewModel = eventViewModel,
                onBack         = { navController.popBackStack() }
            )
        }
    }
}