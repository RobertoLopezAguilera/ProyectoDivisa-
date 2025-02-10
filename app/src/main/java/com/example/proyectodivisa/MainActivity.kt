package com.example.proyectodivisa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.proyectodivisa.api.ExchangeRateApi
import com.example.proyectodivisa.api.ExchangeRateResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/v6/YOUR-API-KEY/latest/USD")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ExchangeRateApi::class.java)
        val call = api.getExchangeRates()

        call.enqueue(object : Callback<ExchangeRateResponse> {
            override fun onResponse(call: Call<ExchangeRateResponse>, response: Response<ExchangeRateResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()
                    data?.let {
                        Log.d("EXCHANGE_RATES", it.conversionRates.toString())
                    }
                } else {
                    Log.e("API_ERROR", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ExchangeRateResponse>, t: Throwable) {
                Log.e("API_ERROR", "Error: ${t.message}")
            }
        })
    }
}
