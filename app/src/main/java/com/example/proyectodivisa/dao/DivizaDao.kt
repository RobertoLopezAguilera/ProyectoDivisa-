package com.example.proyectodivisa.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.proyectodivisa.entities.Diviza

@Dao
interface DivizaDao {

    @Query("SELECT * FROM Diviza_table")
    fun getAllDivizas(): List<Diviza>
}