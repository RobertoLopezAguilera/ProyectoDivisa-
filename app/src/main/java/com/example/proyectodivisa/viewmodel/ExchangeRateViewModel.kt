package com.example.proyectodivisa.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.proyectodivisa.database.ExchangeRateRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.example.proyectodivisa.database.ExchangeRate
import com.example.proyectodivisa.database.UpdateInfo

class ExchangeRateViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ExchangeRateRepository = ExchangeRateRepository(application)
    val exchangeRates: LiveData<List<ExchangeRate>> = repository.getExchangeRates().asLiveData()
    val updateInfo: LiveData<UpdateInfo> = repository.getLatestUpdateInfo().asLiveData()

    init {
        viewModelScope.launch {
            //Inicializa
            repository.syncExchangeRates()
        }
    }
}
