package com.example.pawpal.data.database.dao


import androidx.room.*
import com.example.pawpal.data.database.entities.Usuario

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(usuario: Usuario)

    @Update
    suspend fun update(usuario: Usuario)

    @Delete
    suspend fun delete(usuario: Usuario)

    @Query("SELECT * FROM Usuario WHERE correo = :correo AND contrasena = :contrasena")
    suspend fun getUsuarioByCorreoAndContrasena(correo: String, contrasena: String): Usuario?

    @Query("SELECT * FROM Usuario WHERE correo = :correo")
    suspend fun getUsuarioByCorreo(correo: String): Usuario?

    @Query("SELECT * FROM Usuario")
    suspend fun getAllUsuarios(): List<Usuario>

    @Query("SELECT EXISTS(SELECT * FROM Usuario WHERE correo = :email)")
    suspend fun isEmailRegistered(email: String): Boolean
}