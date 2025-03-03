package com.example.proyectodivisa.database

import com.example.proyectodivisa.model.ExchangeRateResponse
import retrofit2.http.GET

interface ExchangeRateApi {
    @GET("v6/7298488e535953dbbefee5c9/latest/MXN")
    suspend fun getExchangeRates(): ExchangeRateResponse
}