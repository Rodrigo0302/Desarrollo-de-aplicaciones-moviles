package com.example.pawpal.data.database.dao

import androidx.room.*
import com.example.pawpal.data.database.entities.Vacuna


@Dao
interface VacunaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vacuna: Vacuna)

    @Update
    suspend fun update(vacuna: Vacuna)

    @Delete
    suspend fun delete(vacuna: Vacuna)

    @Query("SELECT * FROM Vacuna WHERE mascotaId = :mascotaId")
    suspend fun getVacunasByMascota(mascotaId: Int): List<Vacuna>
}
