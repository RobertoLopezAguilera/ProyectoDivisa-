package com.example.proyectodivisa.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyectodivisa.database.ExchangeRateRepository
import com.example.proyectodivisa.database.ExchangeRate
import com.example.proyectodivisa.database.UpdateInfo
import kotlinx.coroutines.launch

class ExchangeRateViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ExchangeRateRepository = ExchangeRateRepository(application)
    val exchangeRates: LiveData<List<ExchangeRate>> = repository.getExchangeRates().asLiveData()
    val updateInfo: LiveData<UpdateInfo> = repository.getLatestUpdateInfo().asLiveData()

    private val _historicalRates = MutableLiveData<List<ExchangeRate>>()
    val historicalRates: LiveData<List<ExchangeRate>> = _historicalRates

    init {
        viewModelScope.launch {
            repository.syncExchangeRates()
        }
    }

    // Método para obtener los datos históricos desde el ContentProvider
    fun loadHistoricalRates(context: Context, currency: String, startDate: String, endDate: String) {
        viewModelScope.launch {
            val uri = Uri.parse("content://com.example.proyectodivisa.provider/exchange_rates/$currency?start=$startDate&end=$endDate")
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            val rates = mutableListOf<ExchangeRate>()

            cursor?.use {
                val currencyIndex = it.getColumnIndex("currency")
                val rateIndex = it.getColumnIndex("rate")

                while (it.moveToNext()) {
                    val currency = it.getString(currencyIndex)
                    val rate = it.getDouble(rateIndex)
                    rates.add(ExchangeRate(currency, rate))
                }
            }

            _historicalRates.postValue(rates)
        }
    }
}
