package com.example.pawpal.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.data.database.entities.Paseo

class PaseosAdapter : RecyclerView.Adapter<PaseosAdapter.PaseoViewHolder>() {

    private val paseosList = mutableListOf<Paseo>()

    fun submitList(newPaseos: List<Paseo>) {
        paseosList.clear()
        paseosList.addAll(newPaseos)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaseoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_paseo, parent, false)
        return PaseoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PaseoViewHolder, position: Int) {
        val cita = paseosList[position]
        holder.bind(cita)
    }

    override fun getItemCount(): Int = paseosList.size

    inner class PaseoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(paseo: Paseo) {

            //Construir texto para duracion y km
            val duracionKmText = "${paseo.duracion} (${paseo.kmRecorridos} km)"

            itemView.findViewById<TextView>(R.id.tvPaseoTitle).text = paseo.titulo
            itemView.findViewById<TextView>(R.id.tvDescripcionPaseo).text = paseo.descripcion
            itemView.findViewById<TextView>(R.id.tvFechaPaseo).text = paseo.fecha
            itemView.findViewById<TextView>(R.id.tvDuracionKmD).text = duracionKmText
        }
    }
}
