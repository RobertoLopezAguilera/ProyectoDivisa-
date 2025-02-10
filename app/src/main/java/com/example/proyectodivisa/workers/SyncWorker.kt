package com.example.proyectodivisa.workers

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.proyectodivisa.api.ExchangeRateApi
import com.example.proyectodivisa.database.DatabaseHelper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SyncWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ExchangeRateApi::class.java)
        val response = api.getExchangeRates().execute()

        return if (response.isSuccessful) {
            response.body()?.let {
                val dbHelper = DatabaseHelper(applicationContext)
                it.conversionRates.forEach { (currency, rate) ->
                    dbHelper.insertOrUpdateRate(currency, rate)
                }
            }
            Result.success()
        } else {
            Result.failure()
        }
    }
}
