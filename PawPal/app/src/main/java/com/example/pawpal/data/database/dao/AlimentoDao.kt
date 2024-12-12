package com.example.pawpal.data.database.dao

import androidx.room.*
import com.example.pawpal.data.database.entities.Alimento

@Dao
interface AlimentoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alimento: Alimento)

    @Update
    suspend fun update(alimento: Alimento)

    @Delete
    suspend fun delete(alimento: Alimento)

    @Query("SELECT * FROM Alimento WHERE mascotaId = :mascotaId")
    suspend fun getAlimentosByMascota(mascotaId: Int): List<Alimento>
}
