package com.example.pawpal.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.data.database.entities.Cita

class CitasAdapter : RecyclerView.Adapter<CitasAdapter.CitaViewHolder>() {

    private val citasList = mutableListOf<Cita>()

    fun submitList(newCitas: List<Cita>) {
        citasList.clear()
        citasList.addAll(newCitas)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cita, parent, false)
        return CitaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CitaViewHolder, position: Int) {
        val cita = citasList[position]
        holder.bind(cita)
    }

    override fun getItemCount(): Int = citasList.size

    inner class CitaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cita: Cita) {
            //Sepaara la fecha de la hora
            val fechaHora = cita.fechaHora.split(" ")
            //Construir texto para fecha
            val fechaText = "Fecha: ${fechaHora[0]}"
            //Construir texto para hora
            val horaText = "Hora: ${fechaHora[1]}"

            itemView.findViewById<TextView>(R.id.tvCitaTitle).text = cita.asunto
            itemView.findViewById<TextView>(R.id.tvCitaDia).text = fechaText
            itemView.findViewById<TextView>(R.id.tvCitaTime).text = horaText
        }
    }
}
