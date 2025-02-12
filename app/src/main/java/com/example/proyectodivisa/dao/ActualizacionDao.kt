package com.example.proyectodivisa.dao

import androidx.room.*
import com.example.proyectodivisa.entities.Actualizacion

@Dao
interface ActualizacionDao {

    @Query("SELECT * FROM Actualizacion_table")
    fun getAllActualizaciones(): List<Actualizacion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActualizacion(actualizacion: Actualizacion)

    @Update
    fun updateActualizacion(actualizacion: Actualizacion)

    @Delete
    fun deleteActualizacion(actualizacion: Actualizacion)

    @Query("DELETE FROM Actualizacion_table")
    fun deleteAllActualizaciones()
}
