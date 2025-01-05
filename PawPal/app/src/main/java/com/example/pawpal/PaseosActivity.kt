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
import com.example.pawpal.data.database.dao.PaseoDao
import com.example.pawpal.utils.PaseosAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class PaseosActivity : AppCompatActivity() {

    private lateinit var paseoDao: PaseoDao
    private var mascotaId = 1 // Reemplaza con el ID de la mascota actual
    private lateinit var paseosAdapter: PaseosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_paseos)


        //Obtener el id de la mascota del intent
        mascotaId = intent.getIntExtra("mascota_id", -1)


        // Inicializar la base de datos y el DAO
        paseoDao = PawPalDatabase.getDatabase(this).paseoDao()

        // Configurar Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //Cambiar titulo de la toolbar
        supportActionBar?.title = "Paseos de la Mascota"

        // Habilitar botón de "Back"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        // Configurar la acción del botón de "Back"
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Vuelve a la actividad anterior
            finish()
        }

        // Inicializar el RecyclerView
        paseosAdapter = PaseosAdapter()
        val recyclerView: RecyclerView = findViewById(R.id.paseosRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = paseosAdapter

        // Botón para agregar nuevos paseos
        val btnAgregarPaseo: FloatingActionButton = findViewById(R.id.addPaseoButton)
        btnAgregarPaseo.setOnClickListener {
            // Navegar a la actividad de registro de citas
            val intent = Intent(this, RegistroPaseoActivity::class.java)
            intent.putExtra("mascota_id", mascotaId)
            startActivity(intent)

        }

        actualizarPaseos()



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarPaseos()
    }


    private fun actualizarPaseos() {
        lifecycleScope.launch {
            paseoDao.getPaseosByMascota(mascotaId).collect { paseos ->
                paseosAdapter.submitList(paseos)
            }
        }
    }
}