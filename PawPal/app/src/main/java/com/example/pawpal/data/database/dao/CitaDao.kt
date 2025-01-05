package com.example.pawpal.data.database.dao

import androidx.room.*
import com.example.pawpal.data.database.entities.Cita
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM Cita WHERE substr(fechaHora, 1, 10) = :selectedDate AND mascotaId = :mascotaId")
    fun getCitasByDate(selectedDate: String, mascotaId: Int): Flow<List<Cita>>

    @Query("SELECT * FROM Cita WHERE fechaHora > :currentDate  AND mascotaId = :mascotaId ORDER BY fechaHora ASC")
    fun getProximasCitas(currentDate: String, mascotaId: Int): Flow<List<Cita>>

}
