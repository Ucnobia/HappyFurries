package com.example.happyfurries.data.repository

import com.example.happyfurries.data.dao.PetDao
import com.example.happyfurries.data.entities.PetEntity
import com.example.happyfurries.data.network.ApiClient
import com.example.happyfurries.data.network.PetApiModel
import com.example.happyfurries.data.network.toEntity

// El repositorio es el intermediario entre los ViewModels y los datos.
// Ahora tiene dos fuentes: el backend (Retrofit) y la base de datos local (Room).
// Los ViewModels no saben de dónde vienen los datos, solo los piden aquí.

class PetRepository(
    private val petDao: PetDao
) {
    // Referencia al servicio de red que configuré en ApiClient
    private val api = ApiClient.service

    // Pide las mascotas al servidor, las guarda en Room y devuelve la lista.
    // Si el servidor falla por cualquier motivo, devuelvo lo que haya en Room.
    suspend fun getAllPets(): List<PetEntity> {
        return try {
            val petsFromServer = api.getPets()
            // Guardo cada mascota del servidor en Room
            petsFromServer.forEach { petDao.insertPet(it.toEntity()) }
            // Devuelvo lo que hay en Room (ya actualizado)
            petDao.getAllPets()
        } catch (e: Exception) {
            // Si hay error de red, devuelvo los datos locales que tenga
            petDao.getAllPets()
        }
    }

    // Crea una mascota nueva en el servidor y luego la guarda en Room
    suspend fun insertPet(pet: PetEntity) {
        try {
            val petFromServer = api.createPet(pet.toApiModel())
            // Guardo la que devuelve el servidor (tiene el ID real asignado)
            petDao.insertPet(petFromServer.toEntity())
        } catch (e: Exception) {
            // Si falla la red, la guardo solo en local
            petDao.insertPet(pet)
        }
    }

    // Actualiza una mascota en el servidor y en Room
    suspend fun updatePet(pet: PetEntity) {
        try {
            api.updatePet(pet.id, pet.toApiModel())
            petDao.updatePet(pet)
        } catch (e: Exception) {
            // Si falla la red, actualizo solo en local
            petDao.updatePet(pet)
        }
    }

    // Borra una mascota del servidor y de Room
    suspend fun deletePet(pet: PetEntity) {
        try {
            api.deletePet(pet.id)
            petDao.deletePet(pet)
        } catch (e: Exception) {
            // Si falla la red, borro solo en local
            petDao.deletePet(pet)
        }
    }

    // Busca una mascota por ID en Room (ya la tenemos en local)
    suspend fun getPetById(id: Int): PetEntity? = petDao.getPetById(id)
}

// Convierte PetEntity (Room) a PetApiModel (red) para mandarlo al servidor
// Es la operación inversa al toEntity() que definí en PetApiModel.kt
fun PetEntity.toApiModel() = PetApiModel(
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