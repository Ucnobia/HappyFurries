package com.example.happyfurries.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.happyfurries.ui.addpet.AddPetScreen
import com.example.happyfurries.ui.calendarscreen.CalendarScreen
import com.example.happyfurries.ui.petform.PetFormScreen
import com.example.happyfurries.ui.splash.SplashScreen
import com.example.happyfurries.ui.welcome.WelcomeScreen

@Composable
fun AppNavHost(navController: NavHostController) {
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
            PetFormScreen {
                navController.navigate(Routes.CALENDAR)
            }
        }

        composable(Routes.ADD_PET) {
            AddPetScreen()
        }

        composable(Routes.CALENDAR) {
            CalendarScreen(navController)
        }


    }
}



