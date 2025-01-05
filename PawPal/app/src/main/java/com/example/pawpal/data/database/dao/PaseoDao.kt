package com.example.pawpal.data.database.dao

import androidx.room.*
import com.example.pawpal.data.database.entities.Paseo
import kotlinx.coroutines.flow.Flow

@Dao
interface PaseoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(paseo: Paseo)

    @Update
    suspend fun update(paseo: Paseo)

    @Delete
    suspend fun delete(paseo: Paseo)

    @Query("SELECT * FROM Paseo WHERE mascotaId = :mascotaId")
    fun getPaseosByMascota(mascotaId: Int): Flow<List<Paseo>>
}
