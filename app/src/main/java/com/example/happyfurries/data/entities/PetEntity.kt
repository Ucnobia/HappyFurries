package com.example.happyfurries.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val colorHex: String,
    val species: String,
    val breed: String? = null,
    val weightKg: Float,
    val foodBrand: String? = null,
    val foodBagWeightKg: Float? = null,
    val dailyFoodGrams: Int? = null,
    val notes: String? = null
) {
    val initial: String
        get() = name.first().uppercase()
}
