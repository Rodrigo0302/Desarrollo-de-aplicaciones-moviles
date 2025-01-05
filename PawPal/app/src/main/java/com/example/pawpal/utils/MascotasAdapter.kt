package com.example.pawpal.utils

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.R
import com.example.pawpal.data.database.entities.Mascota
import java.io.File

class MascotasAdapter(private val onMascotaClick: (Mascota) -> Unit) : RecyclerView.Adapter<MascotasAdapter.MascotaViewHolder>() {

    private val mascotasList = mutableListOf<Mascota>()

     fun submitList(mascotas: List<Mascota>) {
        mascotasList.clear()
        mascotasList.addAll(mascotas)
         notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MascotaViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mascota, parent, false)
        return MascotaViewHolder(itemView, onMascotaClick)
    }

    override fun onBindViewHolder(holder: MascotaViewHolder, position: Int) {
        holder.bind(mascotasList[position])
    }

    override fun getItemCount() = mascotasList.size

    class MascotaViewHolder(
        itemView: View,
        private val onMascotaClick: (Mascota) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.mascotaImageView)
        private val nombreTextView: TextView = itemView.findViewById(R.id.nombreTextView)
        private val tipoTextView: TextView = itemView.findViewById(R.id.tipoTextView)

        fun bind(mascota: Mascota) {
            val imgFile = File(mascota.foto)
            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                imageView.setImageBitmap(myBitmap)
            } else {
                // Imagen predeterminada si no existe
                imageView.setImageResource(R.drawable.ic_pet_placeholder)
            }

            nombreTextView.text = mascota.nombre
            tipoTextView.text = mascota.tipo

            // Configuracion del clic para cada elemento
            itemView.setOnClickListener {
                onMascotaClick(mascota)
            }

        }
    }
}
