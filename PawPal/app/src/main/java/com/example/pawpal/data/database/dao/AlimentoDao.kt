package com.example.pawpal.data.database.dao

import androidx.room.*
import com.example.pawpal.data.database.entities.Alimento
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM Alimento WHERE mascotaId = :mascotaId AND fecha = :fecha")
    fun getAlimentosByDate(fecha: String, mascotaId: Int): Flow<List<Alimento>>
}
