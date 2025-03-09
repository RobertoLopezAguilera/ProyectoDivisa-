package com.example.proyectodivisa.database

import android.content.Context
import android.util.Log
import com.example.proyectodivisa.database.ExchangeRate
import com.example.proyectodivisa.database.ExchangeRateApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.proyectodivisa.AppDatabase
import com.example.proyectodivisa.model.ExchangeRateDao
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

class ExchangeRateRepository(context: Context) {
    private val api: ExchangeRateApi
    private val dao: ExchangeRateDao

    init {
        val db = AppDatabase.getDatabase(context)
        dao = db.exchangeRateDao()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        api = retrofit.create(ExchangeRateApi::class.java)
    }

    suspend fun syncExchangeRates() {
        withContext(Dispatchers.IO) {
            try {
                val response = api.getExchangeRates()
                Log.d("API_RESPONSE", "Datos obtenidos: ${response.rates}")
                if (response.rates.isNotEmpty()) {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val currentDate = dateFormat.format(Date())

                    val ratesList = response.rates.map { (currency, rate) ->
                        ExchangeRate(
                            currency = currency,
                            rate = rate,
                            date = currentDate
                        )
                    }

                    dao.insertRates(ratesList)
                    dao.insertUpdateInfo(
                        UpdateInfo(
                            lastUpdateUnix = response.lastUpdateUnix,
                            lastUpdateUtc = response.lastUpdateUtc,
                            nextUpdateUnix = response.nextUpdateUnix,
                            nextUpdateUtc = response.nextUpdateUtc
                        )
                    )

                    Log.d("DATABASE", "Datos insertados en SQLite")
                } else {
                    Log.e("API_RESPONSE", "La API no devolvió datos")
                }
            } catch (e: Exception) {
                Log.e("API_RESPONSE", "Error al obtener datos de la API", e)
            }
        }
    }

    // Devuelve Flow en lugar de List
    fun getExchangeRates(): Flow<List<ExchangeRate>> = dao.getAllRates()

    // Devuelve Flow en lugar de UpdateInfo
    fun getLatestUpdateInfo(): Flow<UpdateInfo?> = dao.getLatestUpdateInfo()

    // Devuelve Flow para los últimos registros
    fun getLatestRates(currency: String, limit: Int): Flow<List<ExchangeRate>> = dao.getLatestRates(currency, limit)
}