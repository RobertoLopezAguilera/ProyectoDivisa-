package com.example.proyectodivisa.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyectodivisa.database.ExchangeRateRepository
import com.example.proyectodivisa.database.ExchangeRate
import com.example.proyectodivisa.database.UpdateInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExchangeRateViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ExchangeRateRepository = ExchangeRateRepository(application)

    // Usar Flow y asLiveData()
    val exchangeRates: LiveData<List<ExchangeRate>> = repository.getExchangeRates().asLiveData()
    val updateInfo: LiveData<UpdateInfo?> = repository.getLatestUpdateInfo().asLiveData()

    private val _historicalRates = MutableLiveData<List<ExchangeRate>>()
    val historicalRates: LiveData<List<ExchangeRate>> = _historicalRates

    init {
        viewModelScope.launch {
            repository.syncExchangeRates()
        }
    }

    // Obtener los datos ContentProvider
    fun getHistoricalRates(context: Context, currency: String, startDate: Long, endDate: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val uri = Uri.parse("content://com.example.proyectodivisa.provider/exchange_rates/$currency/$startDate/$endDate")
            val projection = arrayOf("id", "currency", "rate", "date")
            val rates = mutableListOf<ExchangeRate>()

            context.contentResolver.query(uri, projection, null, null, "date ASC")?.use { cursor ->
                val idIndex = cursor.getColumnIndex("id")
                val currencyIndex = cursor.getColumnIndex("currency")
                val rateIndex = cursor.getColumnIndex("rate")
                val dateIndex = cursor.getColumnIndex("date")

                while (cursor.moveToNext()) {
                    val exchangeRate = ExchangeRate(
                        id = cursor.getInt(idIndex),
                        currency = cursor.getString(currencyIndex),
                        rate = cursor.getDouble(rateIndex),
                        date = cursor.getLong(dateIndex)
                    )
                    rates.add(exchangeRate)
                }
            }

            _historicalRates.postValue(rates) // Publicar los resultados en LiveData
        }
    }


}