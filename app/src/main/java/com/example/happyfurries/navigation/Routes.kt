package com.example.happyfurries.navigation

object Routes {
    const val SPLASH = "splash"
    const val WELCOME = "welcome"
    const val PET_FORM = "pet_form"

    // Pantalla principal
    const val MAIN_SCREEN = "main_screen"

    const val ADD_PET = "add_pet"

    // Parámetro dinámico
    const val PET_DETAIL = "pet_detail/{petId}"

    const val PET_SCHEDULE = "pet_schedule"

    // Helper para navegar sin errores
    fun petDetail(petId: Int) = "pet_detail/$petId"
}
