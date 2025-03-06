package com.example.proyectodivisa.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.proyectodivisa.viewmodel.ExchangeRateViewModel
import java.text.NumberFormat
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ExchangeRateScreen(viewModel: ExchangeRateViewModel) {
    val rates by viewModel.exchangeRates.observeAsState(initial = emptyList())
    val updateInfo by viewModel.updateInfo.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Tasas de Cambio") })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            updateInfo?.let {
                Text(
                    "Última Actualización: ${it.lastUpdateUtc}",
                    style = MaterialTheme.typography.body2
                )
                Text(
                    "Próxima Actualización: ${it.nextUpdateUtc}",
                    style = MaterialTheme.typography.body2
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (rates.isEmpty()) {
                Text(
                    "No hay datos disponibles.",
                    color = Color.Red,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                LazyColumn {
                    items(rates) { rate ->
                        CurrencyCard(rate.currency, rate.rate)
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyCard(currency: String, rate: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(MaterialTheme.shapes.medium),
        elevation = 6.dp,
        backgroundColor = Color(0xFFE3F2FD) // Azul claro
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = currency,
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Valor:",
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
            }
            Text(
                text = formatCurrency(rate),
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF0D47A1) // Azul oscuro
            )
        }
    }
}

fun formatCurrency(value: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return format.format(value)
}

