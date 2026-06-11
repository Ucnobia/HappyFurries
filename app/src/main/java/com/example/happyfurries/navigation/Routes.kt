package com.example.happyfurries.navigation

object Routes {
    const val SPLASH       = "splash"
    const val WELCOME      = "welcome"
    const val PET_FORM     = "pet_form"
    const val MAIN_SCREEN  = "main_screen"
    const val ADD_PET      = "add_pet"

    // Detalle, edición y agenda de mascota
    const val PET_DETAIL   = "pet_detail/{petId}"
    const val PET_EDIT     = "pet_edit/{petId}"
    const val PET_SCHEDULE = "pet_schedule/{petId}"

    // Añadir evento — fecha y petId son opcionales como query params
    const val ADD_EVENT    = "add_event?date={date}&petId={petId}"

    // Helpers para navegar sin errores de formato
    fun petDetail(petId: Int)   = "pet_detail/$petId"
    fun petEdit(petId: Int)     = "pet_edit/$petId"
    fun petSchedule(petId: Int) = "pet_schedule/$petId"

    // Navegar a AddEvent con parámetros opcionales
    fun addEvent(date: String? = null, petId: Int? = null): String {
        val d = date ?: ""
        val p = petId?.toString() ?: ""
        return "add_event?date=$d&petId=$p"
    }
    // Editar evento — recibe  eventId como path param
    const val EDIT_EVENT = "edit_event/{eventId}"
    fun editEvent(eventId: Int) = "edit_event/$eventId"
}