package com.example.proyectodivisa.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Diviza_table")
data class Diviza(
    @PrimaryKey
    @ColumnInfo(name = "codigo") val codigo: String, // Código de la divisa (por ejemplo, "USD", "EUR")
    @ColumnInfo(name = "valor") val valor: Double    // Valor de la tasa de cambio
)

