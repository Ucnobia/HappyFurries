package com.example.happyfurries.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.happyfurries.ui.event.AddEventScreen
import com.example.happyfurries.ui.event.EditEventScreen
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
        navController    = navController,
        startDestination = Routes.SPLASH
    ) {
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

        composable(Routes.WELCOME) {
            WelcomeScreen {
                navController.navigate(Routes.PET_FORM)
            }
        }

        composable(Routes.PET_FORM) {
            PetFormScreen(
                viewModel = petViewModel,
                onPetSaved = {
                    onFirstTimeComplete()
                    navController.navigate(Routes.MAIN_SCREEN) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.ADD_PET) {
            AddPetScreen(
                viewModel  = petViewModel,
                onPetSaved = { navController.popBackStack() },
                onBack     = { navController.popBackStack() }
            )
        }

        composable(Routes.MAIN_SCREEN) {
            MainScreen(
                navController  = navController,
                petViewModel   = petViewModel,
                eventViewModel = eventViewModel
            )
        }

        composable(Routes.PET_DETAIL) { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId")?.toInt() ?: 0
            PetDetailScreen(
                petId        = petId,
                petViewModel = petViewModel,
                onEdit       = { navController.navigate(Routes.petEdit(petId)) },
                onBack       = { navController.popBackStack() }
            )
        }

        composable(Routes.PET_EDIT) { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId")?.toInt() ?: 0
            PetEditScreen(
                petId        = petId,
                petViewModel = petViewModel,
                onSaved      = { navController.popBackStack() },
                onBack       = { navController.popBackStack() }
            )
        }

        composable(Routes.PET_SCHEDULE) { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId")?.toInt() ?: 0
            PetScheduleScreen(
                petId          = petId,
                petViewModel   = petViewModel,
                eventViewModel = eventViewModel,
                navController  = navController,
                onBack         = { navController.popBackStack() }
            )
        }

        // AddEvent — recibe fecha y petId como parámetros opcionales
        composable(
            route     = Routes.ADD_EVENT,
            arguments = listOf(
                navArgument("date")  { type = NavType.StringType; defaultValue = "" },
                navArgument("petId") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val date  = backStackEntry.arguments?.getString("date")?.takeIf { it.isNotEmpty() }
            val petId = backStackEntry.arguments?.getString("petId")?.toIntOrNull()
            AddEventScreen(
                eventViewModel = eventViewModel,
                petViewModel   = petViewModel,
                initialDate    = date,
                initialPetId   = petId,
                onSaved        = { navController.popBackStack() },
                onBack         = { navController.popBackStack() }
            )
        }
        // EditEvent — recibe el eventId como path param
        composable(
            route     = Routes.EDIT_EVENT,
            arguments = listOf(
                navArgument("eventId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val eventId = backStackEntry.arguments?.getInt("eventId") ?: 0
            EditEventScreen(
                eventId        = eventId,
                eventViewModel = eventViewModel,
                petViewModel   = petViewModel,
                onSaved        = { navController.popBackStack() },
                onBack         = { navController.popBackStack() }
            )
        }
    }
}