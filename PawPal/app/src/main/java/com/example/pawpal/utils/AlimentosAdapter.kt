package com.example.pawpal.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.data.database.entities.Alimento
import java.util.Locale

class AlimentosAdapter : RecyclerView.Adapter<AlimentosAdapter.AlimentoViewHolder>() {

    private val alimentoList = mutableListOf<Alimento>()

    fun submitList(newAlimentos: List<Alimento>) {
        alimentoList.clear()
        alimentoList.addAll(newAlimentos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlimentoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alimento, parent, false)
        return AlimentoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlimentoViewHolder, position: Int) {
        val alimento = alimentoList[position]
        holder.bind(alimento)
    }

    override fun getItemCount(): Int = alimentoList.size

    inner class AlimentoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(alimento: Alimento) {
            itemView.findViewById<TextView>(R.id.tvNombreTitle).text = alimento.nombre
            itemView.findViewById<TextView>(R.id.tvCantidad).text = String.format(Locale.getDefault(), "%.2f gr", alimento.cantidad)
            itemView.findViewById<TextView>(R.id.tvFechaAlimento).text = alimento.fecha
        }
    }
}
