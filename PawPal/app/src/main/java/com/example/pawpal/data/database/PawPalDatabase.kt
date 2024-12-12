package com.example.pawpal.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.pawpal.data.database.dao.*
import com.example.pawpal.data.database.entities.*

@Database(
    entities = [Usuario::class, Mascota::class, Cita::class, Vacuna::class, Alimento::class, Paseo::class],
    version = 2
)
abstract class PawPalDatabase: RoomDatabase(){
    abstract fun usuarioDao(): UsuarioDao
    abstract fun mascotaDao(): MascotaDao
    abstract fun citaDao(): CitaDao
    abstract fun vacunaDao(): VacunaDao
    abstract fun alimentoDao(): AlimentoDao
    abstract fun paseoDao(): PaseoDao

    companion object {
        @Volatile
        private var INSTANCE: PawPalDatabase? = null

        fun getDatabase(context: Context): PawPalDatabase {
            // Si la instancia ya existe, la devolvemos
            return INSTANCE ?: synchronized(this) {
                // Si no, la creamos usando Room
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PawPalDatabase::class.java,
                    "app_database"
                ).addCallback(object : RoomDatabase.Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        db.execSQL("PRAGMA foreign_keys = ON")
                    }
                })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}