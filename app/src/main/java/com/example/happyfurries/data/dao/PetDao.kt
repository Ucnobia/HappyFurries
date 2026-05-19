package com.example.happyfurries.data.dao

import androidx.room.*
import com.example.happyfurries.data.entities.PetEntity

@Dao
interface PetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetEntity)

    @Update
    suspend fun updatePet(pet: PetEntity)

    @Delete
    suspend fun deletePet(pet: PetEntity)

    @Query("SELECT * FROM pets")
    suspend fun getAllPets(): List<PetEntity>

    @Query("SELECT * FROM pets WHERE id = :petId")
    suspend fun getPetById(petId: Int): PetEntity?
}
