package com.example.proyectodivisa.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Actualizacion_table")
data class Actualizacion(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "actualizacion_id") val id: Int = 0,
    @ColumnInfo(name = "fecha_hora") val fechaHora: String,
    @ColumnInfo(name = "descripcion") val descripcion: String? = null
)
