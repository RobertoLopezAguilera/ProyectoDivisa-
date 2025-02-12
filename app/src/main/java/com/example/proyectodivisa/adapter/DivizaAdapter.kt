package com.example.proyectodivisa.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodivisa.R
import com.example.proyectodivisa.entities.Diviza

class DivizaAdapter(private val divizaList: List<Diviza>) :
    RecyclerView.Adapter<DivizaAdapter.DivizaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DivizaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_diviza, parent, false)
        return DivizaViewHolder(view)
    }

    override fun onBindViewHolder(holder: DivizaViewHolder, position: Int) {
        val diviza = divizaList[position]
        holder.bind(diviza)
    }

    override fun getItemCount(): Int = divizaList.size

    class DivizaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val codigoTextView: TextView = itemView.findViewById(R.id.codigoTextView)
        private val valorTextView: TextView = itemView.findViewById(R.id.valorTextView)

        fun bind(diviza: Diviza) {
            // Accede directamente a las propiedades "codigo" y "valor" de la entidad
            codigoTextView.text = diviza.codigo
            valorTextView.text = diviza.valor.toString()
        }
    }
}
