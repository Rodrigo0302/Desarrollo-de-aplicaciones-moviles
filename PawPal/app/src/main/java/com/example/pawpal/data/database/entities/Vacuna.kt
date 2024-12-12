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
data class Vacuna(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val fechaAplicacion: String, // Tipo de dato LocalDate
    val mascotaId: Int
)
