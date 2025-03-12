package com.example.proyectodivisa.ui.theme

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.example.proyectodivisa.database.ExchangeRate
import com.example.proyectodivisa.viewmodel.ExchangeRateViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import java.text.NumberFormat
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ExchangeRateScreen(viewModel: ExchangeRateViewModel) {
    val rates by viewModel.exchangeRates.observeAsState(initial = emptyList())
    val updateInfo by viewModel.updateInfo.observeAsState()
    val context = LocalContext.current

    var selectedCurrency by remember { mutableStateOf<String?>(null) }
    var startDate by remember { mutableStateOf(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000L) } // Últimos 7 días
    var endDate by remember { mutableStateOf(System.currentTimeMillis()) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tasas de Cambio", color = Color.White) },
                backgroundColor = Color(0xFF1976D2)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            updateInfo?.let {
                Text("Última Actualización: ${it.lastUpdateUtc}", fontSize = 14.sp)
                Text("Próxima Actualización: ${it.nextUpdateUtc}", fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de tasas de cambio actuales con la gráfica expandible
            if (rates.isEmpty()) {
                Text("No hay datos disponibles.", color = Color.Red, fontSize = 16.sp)
            } else {
                LazyColumn {
                    items(rates) { rate ->
                        var isExpanded by remember { mutableStateOf(false) }

                        Column {
                            CurrencyCard(
                                currency = rate.currency,
                                rate = rate.rate,
                                onClick = {
                                    selectedCurrency = if (selectedCurrency == rate.currency) null else rate.currency
                                    isExpanded = !isExpanded
                                }
                            )

                            // Si la moneda está seleccionada, mostramos la gráfica debajo
                            if (isExpanded) {
                                LaunchedEffect(selectedCurrency, startDate, endDate) {
                                    viewModel.getHistoricalRates(context, rate.currency, startDate, endDate)
                                }

                                val historicalRates by viewModel.historicalRates.observeAsState(initial = emptyList())

                                ExchangeRateChart(historicalRates)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExchangeRateChart(rates: List<ExchangeRate>) {
    val context = LocalContext.current

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(16.dp),
        factory = { ctx ->
            LineChart(ctx).apply {
                description.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                axisRight.isEnabled = false
                legend.isEnabled = true
            }
        },
        update = { chart ->
            val entries = rates.mapIndexed { index, rate ->
                Entry(index.toFloat(), rate.rate.toFloat())
            }

            val lineDataSet = LineDataSet(entries, "Tipo de Cambio").apply {
                color = Color(0xFF1976D2).toArgb()
                valueTextColor = Color.Black.toArgb()
                lineWidth = 2f
                setCircleColor(Color(0xFF1976D2).toArgb())
                setDrawFilled(true)
                fillColor = Color(0xFF64B5F6).toArgb()
                fillAlpha = 100
            }

            chart.data = LineData(lineDataSet)
            chart.invalidate() // Refrescar gráfico
        }
    )
}

@Composable
fun CurrencyCard(currency: String, rate: Double, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        elevation = 6.dp,
        backgroundColor = Color(0xFFE3F2FD)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = currency, style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Valor:", style = MaterialTheme.typography.body2, color = Color.Gray)
            }
            Text(
                text = formatCurrency(rate),
                style = MaterialTheme.typography.h6.copy(fontSize = 18.sp),
                color = Color(0xFF0D47A1)
            )
        }
    }
}

fun formatCurrency(value: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return format.format(value)
}