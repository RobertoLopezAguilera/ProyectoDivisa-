package com.example.proyectodivisa.model

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.example.proyectodivisa.database.ExchangeRate
import com.example.proyectodivisa.database.UpdateInfo

@Dao
interface ExchangeRateDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRates(rates: List<ExchangeRate>)

    @Query("SELECT * FROM exchange_rates WHERE currency = :currency BETWEEN :startDate AND :endDate")
    fun getExchangeRatesInRangeCursor(currency: String, startDate: Long, endDate: Long): Cursor

    @Query("SELECT * FROM exchange_rates WHERE currency = :currency ORDER BY date DESC LIMIT 10")
    fun getLast10ExchangeRatesByCurrency(currency: String): Cursor


    @Query("SELECT * FROM exchange_rates WHERE currency = :currency ORDER BY date DESC LIMIT :limit")
    fun getLatestRates(currency: String, limit: Int): Flow<List<ExchangeRate>> // Usar Flow

    @Query("SELECT * FROM exchange_rates")
    fun getAllRates(): Flow<List<ExchangeRate>> // Usar Flow

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateInfo(updateInfo: UpdateInfo)

    @Query("SELECT * FROM update_info ORDER BY id DESC LIMIT 1")
    fun getLatestUpdateInfo(): Flow<UpdateInfo?> // Usar Flow
}