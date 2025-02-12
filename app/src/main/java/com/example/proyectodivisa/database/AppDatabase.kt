package com.example.proyectodivisa.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.proyectodivisa.dao.DivizaDao
import com.example.proyectodivisa.entities.Diviza

@Database(entities = [Diviza::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun divizaDao(): DivizaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "diviza_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
