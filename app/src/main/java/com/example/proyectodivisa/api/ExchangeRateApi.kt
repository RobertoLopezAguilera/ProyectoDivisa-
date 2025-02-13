package com.example.proyectodivisa.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface ExchangeRateApi {
    @GET("v6/7298488e535953dbbefee5c9/latest/USD")
    fun getExchangeRates(): Call<ApiResponse>
}

object RetrofitClient {
    private const val BASE_URL = "https://v6.exchangerate-api.com/"

    val api: ExchangeRateApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExchangeRateApi::class.java)
    }
}
