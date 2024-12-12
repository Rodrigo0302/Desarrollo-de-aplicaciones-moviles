package com.example.pawpal.data.database.dao

import androidx.room.*
import com.example.pawpal.data.database.entities.Cita

@Dao
interface CitaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cita: Cita)

    @Update
    suspend fun update(cita: Cita)

    @Delete
    suspend fun delete(cita: Cita)

    @Query("SELECT * FROM Cita WHERE mascotaId = :mascotaId")
    suspend fun getCitasByMascota(mascotaId: Int): List<Cita>
}
