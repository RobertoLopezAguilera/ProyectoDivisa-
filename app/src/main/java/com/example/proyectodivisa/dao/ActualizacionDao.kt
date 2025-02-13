package com.example.proyectodivisa.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.proyectodivisa.entities.Actualizacion

@Dao
interface ActualizacionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActualizacion(actualizacion: Actualizacion)

    @Query("SELECT * FROM Actualizacion_table ORDER BY actualizacion_id DESC LIMIT 1")
    fun getLatestActualizacion(): Actualizacion?
}