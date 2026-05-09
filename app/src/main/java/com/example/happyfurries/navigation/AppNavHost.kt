package com.example.happyfurries.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.happyfurries.ui.viewmodel.PetViewModel
import com.example.happyfurries.ui.viewmodel.EventViewModel
import com.example.happyfurries.ui.pet.AddPetScreen
import com.example.happyfurries.ui.pet.PetDetailScreen
import com.example.happyfurries.ui.pet.PetFormScreen
import com.example.happyfurries.ui.pet.PetScheduleScreen
import com.example.happyfurries.ui.splash.SplashScreen
import com.example.happyfurries.ui.welcome.WelcomeScreen
import com.example.happyfurries.ui.main.MainScreen

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
        composable(Routes.SPLASH) {
            SplashScreen {
                navController.navigate(Routes.WELCOME) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
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
                    navController.navigate(Routes.MAIN_SCREEN)
                }
            )
        }

        composable(Routes.ADD_PET) {
            AddPetScreen(
                viewModel = petViewModel,
                onPetSaved = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.MAIN_SCREEN) {
            MainScreen(
                navController = navController,
                petViewModel = petViewModel,
                eventViewModel = eventViewModel
            )
        }

        composable(Routes.PET_DETAIL) { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId")?.toInt() ?: 0

            PetDetailScreen(
                petId = petId,
                petViewModel = petViewModel
            )
        }

        composable(Routes.PET_SCHEDULE) {
            PetScheduleScreen(
                eventViewModel = eventViewModel
            )
        }
    }
}
