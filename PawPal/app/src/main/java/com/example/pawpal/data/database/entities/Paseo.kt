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
    val duracion: Int,
    val kmRecorridos: Double,
    val fecha: String, // Tipo de dato LocalDate
    val mascotaId: Int
)
