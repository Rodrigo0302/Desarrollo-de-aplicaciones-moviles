package com.example.pawpal.data.database.dao

import androidx.room.*
import com.example.pawpal.data.database.entities.Paseo

@Dao
interface PaseoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(paseo: Paseo)

    @Update
    suspend fun update(paseo: Paseo)

    @Delete
    suspend fun delete(paseo: Paseo)

    @Query("SELECT * FROM Paseo WHERE mascotaId = :mascotaId")
    suspend fun getPaseosByMascota(mascotaId: Int): List<Paseo>
}
