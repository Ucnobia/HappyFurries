package com.example.happyfurries.data.repository

import com.example.happyfurries.data.dao.PetDao
import com.example.happyfurries.data.entities.PetEntity

class PetRepository(
    private val petDao: PetDao
) {

    suspend fun insertPet(pet: PetEntity) = petDao.insertPet(pet)

    suspend fun updatePet(pet: PetEntity) = petDao.updatePet(pet)

    suspend fun deletePet(pet: PetEntity) = petDao.deletePet(pet)

    suspend fun getAllPets(): List<PetEntity> = petDao.getAllPets()

    suspend fun getPetById(id: Int): PetEntity? = petDao.getPetById(id)
}
