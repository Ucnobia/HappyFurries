package com.example.happyfurries.data.network

import com.google.gson.annotations.SerializedName

// Datos backend en JSON.
// Retrofit -->en objetos Kotlin.

// Modelo de mascota tal como llega del servidor
data class PetApiModel(
    @SerializedName("id")              val id: Int,           // ID único de la mascota
    @SerializedName("name")            val name: String,      // Nombre
    @SerializedName("species")         val species: String,   // Especie (Dog, Cat...)
    @SerializedName("breed")           val breed: String?,    // Raza, puede ser null
    @SerializedName("colorHex")        val colorHex: String,  // Color del avatar en hex
    @SerializedName("weightKg")        val weightKg: Float,   // Peso en kilos
    @SerializedName("foodBrand")       val foodBrand: String?,       // Marca de comida
    @SerializedName("foodBagWeightKg") val foodBagWeightKg: Float?,  // Peso del saco
    @SerializedName("dailyFoodGrams")  val dailyFoodGrams: Int?,     // Gramos al día
    @SerializedName("notes")           val notes: String?            // Notas adicionales
)

// --> lo que llega del backend -->objeto que Room -->guardar
// Usa repositorio después de recibir la respuesta del servidor
fun PetApiModel.toEntity() = com.example.happyfurries.data.entities.PetEntity(
    id             = this.id,
    name           = this.name,
    species        = this.species,
    breed          = this.breed,
    colorHex       = this.colorHex,
    weightKg       = this.weightKg,
    foodBrand      = this.foodBrand,
    foodBagWeightKg = this.foodBagWeightKg,
    dailyFoodGrams  = this.dailyFoodGrams,
    notes          = this.notes
)

// Modelo de evento --> servidor
data class EventApiModel(
    @SerializedName("id")          val id: Int,
    @SerializedName("petId")       val petId: Int,        // A qué mascota pertenece
    @SerializedName("title")       val title: String,     // Título del evento
    @SerializedName("date")        val date: String,      // Fecha en formato YYYY-MM-DD
    @SerializedName("time")        val time: String,      // Hora en formato HH:mm
    @SerializedName("description") val description: String? // Descripción opcional
)

//  Modelo de red --> de Room
fun EventApiModel.toEntity() = com.example.happyfurries.data.entities.EventEntity(
    id          = this.id,
    petId       = this.petId,
    title       = this.title,
    date        = this.date,
    time        = this.time,
    description = this.description
)