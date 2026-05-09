package com.example.happyfurries.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val petId: Int,            // Mascota a la que pertenece el evento
    val date: String,          // LocalDate como String (YYYY-MM-DD)
    val time: String,          //HH:mm
    val title: String,         // Título del evento
    val description: String? = null
)
