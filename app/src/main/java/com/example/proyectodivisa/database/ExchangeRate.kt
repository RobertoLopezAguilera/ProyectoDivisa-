package com.example.proyectodivisa.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates")
data class ExchangeRate(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val currency: String,
    val rate: Double,
    val date: Long
) {
    constructor() : this(0, "", 0.0, 0L) //Constructor vacío para Room
}


@Entity(tableName = "update_info")
data class UpdateInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val lastUpdateUnix: Long,
    val lastUpdateUtc: String,
    val nextUpdateUnix: Long,
    val nextUpdateUtc: String
)