package com.example.proyectodivisa

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodivisa.adapter.DivizaAdapter
import com.example.proyectodivisa.api.ApiResponse
import com.example.proyectodivisa.api.RetrofitClient
import com.example.proyectodivisa.dao.DivizaDao
import com.example.proyectodivisa.database.AppDatabase
import com.example.proyectodivisa.entities.Diviza
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var divizaDao: DivizaDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var textViewJson: TextView
    private lateinit var divizaAdapter: DivizaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configurar TextView para mostrar el JSON
        textViewJson = findViewById(R.id.textViewJson)

        // Inicializar el adaptador
        divizaAdapter = DivizaAdapter(emptyList())
        recyclerView.adapter = divizaAdapter

        // Obtener instancia de la base de datos
        val db = AppDatabase.getDatabase(this)
        divizaDao = db.divizaDao()

        // Verificar conexión a Internet y cargar datos
        lifecycleScope.launch {
            if (isInternetAvailable()) { // Llamar a la función de extensión
                fetchAndDisplayApiData() // Si hay Internet, obtener datos de la API
            } else {
                loadAndDisplayDivizas() // Si no hay Internet, cargar datos de Room
            }
        }
    }

    // Función de extensión para verificar la conexión a Internet
    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private suspend fun fetchAndDisplayApiData() {
        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getExchangeRates().execute()
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    withContext(Dispatchers.Main) {
                        if (apiResponse != null) {
                            // Mostrar el JSON en el TextView
                            textViewJson.text = apiResponse.toString()

                            // Convertir la respuesta de la API a una lista de Diviza
                            val divizas = apiResponse.toDivizaList()

                            // Insertar o actualizar datos en Room
                            insertOrUpdateDivizas(divizas)

                            // Mostrar los datos en el RecyclerView
                            divizaAdapter.updateData(divizas)
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@MainActivity,
                            "Error en la API: ${response.errorBody()?.string()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private suspend fun insertOrUpdateDivizas(divizas: List<Diviza>) {
        withContext(Dispatchers.IO) {
            divizas.forEach { diviza ->
                divizaDao.insertDiviza(diviza) // Usa OnConflictStrategy.REPLACE para actualizar
            }
        }
    }

    private suspend fun loadAndDisplayDivizas() {
        withContext(Dispatchers.IO) {
            val divizas = divizaDao.getAllDivizas()
            withContext(Dispatchers.Main) {
                if (divizas.isNotEmpty()) {
                    divizaAdapter.updateData(divizas)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "No hay datos en la base de datos.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    // Función para convertir la respuesta de la API a una lista de Diviza
    private fun ApiResponse.toDivizaList(): List<Diviza> {
        return this.conversion_rates.map { (currency, rate) ->
            Diviza(currency, rate)
        }
    }
}