package com.example.happyfurries.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.happyfurries.data.dao.PetDao
import com.example.happyfurries.data.dao.EventDao
import com.example.happyfurries.data.entities.PetEntity
import com.example.happyfurries.data.entities.EventEntity

@Database(
    entities = [PetEntity::class, EventEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao
    abstract fun eventDao(): EventDao
}
