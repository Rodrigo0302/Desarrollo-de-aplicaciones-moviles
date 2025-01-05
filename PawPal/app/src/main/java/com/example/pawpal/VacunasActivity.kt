package com.example.pawpal

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.data.database.PawPalDatabase
import com.example.pawpal.data.database.dao.VacunaDao
import com.example.pawpal.utils.VacunasAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class VacunasActivity : AppCompatActivity() {
    private lateinit var vacunaDao: VacunaDao
    private var mascotaId = -1 // Reemplaza con el ID de la mascota actual
    private lateinit var vacunaAdapter: VacunasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_vacunas)

        // Obtener el ID de la mascota del intent
        mascotaId = intent.getIntExtra("mascota_id", -1)

        // Inicializar la base de datos y el DAO
        vacunaDao = PawPalDatabase.getDatabase(this).vacunaDao()

        // Configurar Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //Cambiar titulo de la toolbar
        supportActionBar?.title = "Vacunas de la Mascota"

        // Habilitar botón de "Back"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        // Configurar la acción del botón de "Back"
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Vuelve a la actividad anterior
            finish()
        }

        // Inicializar el RecyclerView
        vacunaAdapter = VacunasAdapter()
        val recyclerView: RecyclerView = findViewById(R.id.vacunasRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = vacunaAdapter

        //Boton para agregar nueva vacuna
        val btnAgregarVacuna: FloatingActionButton = findViewById(R.id.addVacunaButton)
        btnAgregarVacuna.setOnClickListener {
            // Navegar a la actividad de registro de vacunas
            val intent = Intent(this, RegistroVacunaActivity::class.java)
            intent.putExtra("mascota_id", mascotaId)
            startActivity(intent)
        }

        //Obtener todas las vacunas de la mascota
        actualizarVacunas()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun actualizarVacunas(){
        lifecycleScope.launch {
            val vacunas = vacunaDao.getVacunasByMascota(mascotaId)
            vacunaAdapter.submitList(vacunas)
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarVacunas()
    }
}