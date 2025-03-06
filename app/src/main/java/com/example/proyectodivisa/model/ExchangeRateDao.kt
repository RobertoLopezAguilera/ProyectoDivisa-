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
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRates(rates: List<ExchangeRate>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpdateInfo(updateInfo: UpdateInfo)

    @Query("SELECT * FROM exchange_rates")
    fun getAllRates(): Flow<List<ExchangeRate>>

    @Query("SELECT * FROM update_info ORDER BY id DESC LIMIT 1")
    fun getLatestUpdateInfo(): Flow<UpdateInfo>

    @Query("SELECT * FROM exchange_rates WHERE currency = :currency")
    fun getExchangeRateCursor(currency: String): Cursor

    @Query("SELECT * FROM update_info WHERE lastUpdateUnix BETWEEN :startDate AND :endDate")
    fun getExchangeRatesInRangeCursor(startDate: Long, endDate: Long): Cursor

    @Query("SELECT * FROM exchange_rates WHERE currency = :currency DESC LIMIT :limit")
    fun getLatestExchangeRates(currency: String, limit: Int): List<ExchangeRate>
}