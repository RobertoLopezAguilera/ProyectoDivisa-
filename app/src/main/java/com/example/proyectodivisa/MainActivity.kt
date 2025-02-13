package com.example.proyectodivisa

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodivisa.adapter.DivizaAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Configurar TextView para mostrar el JSON
        textViewJson = findViewById(R.id.textViewJson)

        // Obtener instancia de la base de datos
        val db = AppDatabase.getDatabase(this)
        divizaDao = db.divizaDao()

        // Llamar a la API y mostrar resultados
        lifecycleScope.launch {
            fetchAndDisplayApiData()
            loadAndDisplayDivizas()
        }
    }

    private suspend fun fetchAndDisplayApiData() {
        withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getExchangeRates().execute()
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    withContext(Dispatchers.Main) {
                        textViewJson.text = apiResponse.toString() // Mostrar el JSON en el TextView
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

    private suspend fun loadAndDisplayDivizas() {
        withContext(Dispatchers.IO) {
            val divizas = divizaDao.getAllDivizas()
            withContext(Dispatchers.Main) {
                if (divizas.isNotEmpty()) {
                    recyclerView.adapter = DivizaAdapter(divizas)
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
}
