package com.example.pawpal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.data.database.PawPalDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MascotasActivity : AppCompatActivity() {
    private lateinit var rvMascotas: RecyclerView
    private lateinit var mascotasAdapter: MascotasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mascotas)

        // Configurar Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Habilitar botón de "Back"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        rvMascotas = findViewById(R.id.mascotasRecyclerView)
        rvMascotas.layoutManager = LinearLayoutManager(this)

        // Configurar el adaptador del RecyclerView
        mascotasAdapter = MascotasAdapter { mascota ->
            // Navegar a la pantalla de detalle con los datos de la mascota seleccionada
            val intent = Intent(this, DetallesMascotaActivity::class.java).apply {
                putExtra("mascota_id", mascota.id) // Atributo necesario para identificar a la mascota
            }
            startActivity(intent)
            finish()
        }
        rvMascotas.adapter = mascotasAdapter

        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()

        //consultarMascotas(userEmail)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (userEmail != null) {
            lifecycleScope.launch {
                try {
                    val mascotas = withContext(Dispatchers.IO) {
                        PawPalDatabase.getDatabase(this@MascotasActivity)
                            .mascotaDao()
                            .getMascotasByUsuario(userEmail)
                    }
                    if (mascotas.isNotEmpty()) {
                        mascotasAdapter.submitList(mascotas)
                        Log.d("MascotasActivity", "Mascotas cargadas: ${mascotas.size}")
                    } else {
                        Log.d("MascotasActivity", "No se encontraron mascotas")
                        Toast.makeText(this@MascotasActivity, "No hay mascotas registradas", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("MascotasActivity", "Error al cargar mascotas: ${e.message}")
                    Toast.makeText(this@MascotasActivity, "Error al cargar mascotas", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Manejar error de sesión
            Toast.makeText(this, "No se pudo cargar la sesión", Toast.LENGTH_SHORT).show()
        }

        val botonAgregarMascota: FloatingActionButton = findViewById(R.id.addMascotaButton)
        botonAgregarMascota.setOnClickListener {
            val intent = Intent(this, RegistroMascotaActivity::class.java)
            startActivity(intent)
        }

        // Configurar la acción del botón de "Back"
        toolbar.setNavigationOnClickListener {
            sessionManager.clearSession()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }


}