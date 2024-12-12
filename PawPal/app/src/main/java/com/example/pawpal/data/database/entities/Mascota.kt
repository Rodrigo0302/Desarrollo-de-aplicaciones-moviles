package com.example.pawpal.data.database.entities
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = arrayOf("correo"),
            childColumns = arrayOf("usuarioCorreo"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )]
)
data class Mascota(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val tipo: String,
    val raza: String,
    val edad: Int,
    val peso: Double,
    val foto: String,
    val usuarioCorreo: String
)
