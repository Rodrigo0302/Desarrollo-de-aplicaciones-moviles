package com.example.pawpal.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Mascota::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("mascotaId"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class Paseo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val descripcion: String,
    val duracion: String,
    val kmRecorridos: Double,
    val fecha: String,
    val mascotaId: Int
)
