package com.example.proyectodivisa.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.proyectodivisa.database.ExchangeRateRepository


class CurrencySyncWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    //sincroniza la bd
    override suspend fun doWork(): Result {
        return try {
            //Consulta el metodo para sincronizar la bd
            val repository = ExchangeRateRepository(applicationContext)
            repository.syncExchangeRates()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}