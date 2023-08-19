package com.example.model.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.model.data.Product

@Database(entities = [Product::class], version = 1, exportSchema = true)
abstract class AppDatabase:RoomDatabase() {

    abstract fun productDao():ProductDao
}