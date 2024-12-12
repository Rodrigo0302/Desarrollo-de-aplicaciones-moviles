package com.example.pawpal.data.database.dao

import androidx.room.*
import com.example.pawpal.data.database.entities.Mascota

@Dao
interface MascotaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mascota: Mascota)

    @Update
    suspend fun update(mascota: Mascota)

    @Delete
    suspend fun delete(mascota: Mascota)

    @Query("SELECT * FROM Mascota WHERE usuarioCorreo = :correoUsuario ORDER BY nombre ASC")
    suspend fun getMascotasByUsuario(correoUsuario: String): List<Mascota>

    @Query("SELECT * FROM Mascota WHERE id = :id")
    suspend fun getMascotaById(id: Int): Mascota?

    @Query("DELETE FROM Mascota WHERE id = :mascotaId")
    suspend fun deleteMascotaById(mascotaId: Int)
}
