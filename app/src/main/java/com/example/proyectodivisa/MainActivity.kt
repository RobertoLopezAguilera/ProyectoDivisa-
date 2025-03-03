package com.example.proyectodivisa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.*
import com.example.proyectodivisa.ui.theme.ExchangeRateScreen
import com.example.proyectodivisa.viewmodel.ExchangeRateViewModel
import com.example.proyectodivisa.workers.CurrencySyncWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //WorkManager
        val workRequest = PeriodicWorkRequestBuilder<CurrencySyncWorker>(
            24, TimeUnit.HOURS
        )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "CurrencySyncWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        setContent {
            val viewModel: ExchangeRateViewModel = viewModel()
            ExchangeRateScreen(viewModel)
        }
    }
}
