package com.example.pawpal

import android.content.Intent
import android.os.Bundle
import android.widget.CalendarView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pawpal.data.database.PawPalDatabase
import com.example.pawpal.data.database.dao.AlimentoDao
import com.example.pawpal.utils.AlimentosAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.util.Locale

class AlimentacionActivity : AppCompatActivity() {
    private lateinit var calendarView: CalendarView
    private lateinit var alimentoDao: AlimentoDao
    private var mascotaId = -1 // Reemplaza con el ID de la mascota actual
    private lateinit var alimentoAdapter: AlimentosAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_alimentacion)

        //Obtener el id de la mascota del intent
        mascotaId = intent.getIntExtra("mascota_id", -1)


        // Inicializar la base de datos y el DAO
        alimentoDao = PawPalDatabase.getDatabase(this).alimentoDao()

        // Configurar Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //Cambiar titulo de la toolbar
        supportActionBar?.title = "Alimentación de la Mascota"

        // Habilitar botón de "Back"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        // Configurar la acción del botón de "Back"
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Vuelve a la actividad anterior
            finish()
        }


        // Inicializar el RecyclerView
        alimentoAdapter = AlimentosAdapter()
        val recyclerView: RecyclerView = findViewById(R.id.alimentacionRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = alimentoAdapter

        // Configurar el calendario
        calendarView = findViewById(R.id.calendarViewAlimentos)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Convertir la fecha seleccionada a "yyyy-MM-dd"
            val selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth)

            //Log.d("AlimentacionActivity", "Fecha seleccionada: $selectedDate")

            // Consultar alimentos para la fecha seleccionada
            lifecycleScope.launch {
                alimentoDao.getAlimentosByDate(selectedDate, mascotaId).collect { alimentos ->
                    alimentoAdapter.submitList(alimentos)
                }
            }
        }

        //Boton para agregar nuevo alimento
        val btnAgregarAlimento: FloatingActionButton = findViewById(R.id.addAlimentoButton)
        btnAgregarAlimento.setOnClickListener {
            // Navegar a la actividad de registro de alimentos
            val intent = Intent(this, RegistroAlimentoActivity::class.java)
            intent.putExtra("mascota_id", mascotaId)
            startActivity(intent)
        }

        //Obtener todos los alimentos de la mascota
        actualizarAlimentos()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun actualizarAlimentos(){
        lifecycleScope.launch {
            val alimentos = alimentoDao.getAlimentosByMascota(mascotaId)
            alimentoAdapter.submitList(alimentos)
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarAlimentos()
    }
}