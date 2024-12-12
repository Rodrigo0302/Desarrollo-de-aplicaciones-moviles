package com.example.pawpal.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Usuario(
    @PrimaryKey val correo: String, // El correo es la clave primaria
    val nombre: String,
    val fechaNacimiento: String, // Tipo de dato LocalDate
    val contrasena: String
)