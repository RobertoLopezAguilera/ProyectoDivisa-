package com.example.proyectodivisa.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.proyectodivisa.databinding.ItemDivizaBinding
import com.example.proyectodivisa.entities.Diviza

class DivizaAdapter(private val divizas: List<Diviza>) : RecyclerView.Adapter<DivizaAdapter.DivizaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DivizaViewHolder {
        val binding = ItemDivizaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DivizaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DivizaViewHolder, position: Int) {
        val diviza = divizas[position]
        holder.bind(diviza)
    }

    override fun getItemCount(): Int {
        return divizas.size
    }

    class DivizaViewHolder(private val binding: ItemDivizaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(diviza: Diviza) {
            binding.tvDivizaCode.text = diviza.codigo
            binding.tvDivizaName.text = diviza.valor.toString()
        }
    }
}
