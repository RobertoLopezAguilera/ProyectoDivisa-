package com.example.proyectodivisa.dao

import androidx.room.*
import com.example.proyectodivisa.entities.Diviza

@Dao
interface DivizaDao {

    @Query("SELECT * FROM Diviza_table")
    fun getAllDivizas(): List<Diviza>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDiviza(diviza: Diviza)

    @Update
    fun updateDiviza(diviza: Diviza)

    @Delete
    fun deleteDiviza(diviza: Diviza)

    @Query("DELETE FROM Diviza_table")
    fun deleteAllDivizas()
}
