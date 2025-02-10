package com.example.proyectodivisa.api

import retrofit2.Call
import retrofit2.http.GET

interface ExchangeRateApi {
    @GET("7298488e535953dbbefee5c9") // Reemplaza con tu API key
    fun getExchangeRates(): Call<ExchangeRateResponse>
}
