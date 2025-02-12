package com.example.proyectodivisa

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodivisa.adapter.DivizaAdapter
import com.example.proyectodivisa.dao.DivizaDao
import com.example.proyectodivisa.database.AppDatabase
import com.example.proyectodivisa.entities.Diviza
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var divizaDao: DivizaDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configurar RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Obtener instancia de la base de datos
        val db = AppDatabase.getDatabase(this)
        divizaDao = db.divizaDao()

        // Cargar datos de la base de datos
        lifecycleScope.launch {
            val divizas = loadDivizas()
            recyclerView.adapter = DivizaAdapter(divizas)
        }
    }

    private suspend fun loadDivizas(): List<Diviza> {
        return withContext(Dispatchers.IO) {
            divizaDao.getAllDivizas()
        }
    }
}
