package com.example.proyectodivisa.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Diviza_table")
data class Diviza(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "diviza_id") val id: Int = 0,
    @ColumnInfo(name = "codigo") val codigo: String,
    @ColumnInfo(name = "valor") val valor: Double
)
