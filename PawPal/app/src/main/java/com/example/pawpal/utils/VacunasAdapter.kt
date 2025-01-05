package com.example.pawpal.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.data.database.entities.Alimento
import com.example.pawpal.data.database.entities.Vacuna
import java.util.Locale

class VacunasAdapter : RecyclerView.Adapter<VacunasAdapter.VacunaViewHolder>() {

    private val vacunaList = mutableListOf<Vacuna>()

    fun submitList(newVacunas: List<Vacuna>) {
        vacunaList.clear()
        vacunaList.addAll(newVacunas)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacunaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vacuna, parent, false)
        return VacunaViewHolder(view)
    }

    override fun onBindViewHolder(holder: VacunaViewHolder, position: Int) {
        val vacuna = vacunaList[position]
        holder.bind(vacuna)
    }

    override fun getItemCount(): Int = vacunaList.size

    inner class VacunaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(vacuna: Vacuna){
            itemView.findViewById<TextView>(R.id.tvNombreVacunaTitle).text = vacuna.nombre
            itemView.findViewById<TextView>(R.id.tvFechaVacuna).text = vacuna.fechaAplicacion
        }
    }
}
