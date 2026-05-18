package com.example.happyfurries.navigation

object Routes {
    const val SPLASH = "splash"
    const val WELCOME = "welcome"
    const val PET_FORM = "pet_form"

    // Pantalla principal
    const val MAIN_SCREEN = "main_screen"

    const val ADD_PET = "add_pet"

    // Detalle de mascota — recibe el ID como parámetro dinámico
    const val PET_DETAIL = "pet_detail/{petId}"

    // Editar mascota — también recibe el ID para cargar los datos actuales
    const val PET_EDIT = "pet_edit/{petId}"

    const val PET_SCHEDULE = "pet_schedule/{petId}"

    // Helpers para navegar pasando el ID sin errores de formato
    fun petDetail(petId: Int) = "pet_detail/$petId"
    fun petEdit(petId: Int)   = "pet_edit/$petId"
    fun petSchedule(petId: Int) = "pet_schedule/$petId"
}