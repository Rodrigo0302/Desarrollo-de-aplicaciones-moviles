package com.example.pawpal

import com.example.pawpal.utils.CitasAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.EventDay
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.example.pawpal.data.database.PawPalDatabase
import com.example.pawpal.data.database.dao.CitaDao
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class CitasActivity : AppCompatActivity() {

    private lateinit var calendarView: com.applandeo.materialcalendarview.CalendarView
    private lateinit var citaDao: CitaDao
    private var mascotaId = 1 // Reemplaza con el ID de la mascota actual

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_citas)

        //Obtener el id de la mascota del intent
        mascotaId = intent.getIntExtra("mascota_id", -1)


        // Inicializar la base de datos y el DAO
        citaDao = PawPalDatabase.getDatabase(this).citaDao()

        // Configurar Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        //Cambiar titulo de la toolbar
        supportActionBar?.title = "Citas de la Mascota"

        // Habilitar botón de "Back"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        // Configurar la acción del botón de "Back"
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Vuelve a la actividad anterior
            finish()
        }


        // Inicializar el RecyclerView
        val citasAdapter = CitasAdapter()
        val recyclerView: RecyclerView = findViewById(R.id.rvProximasCitas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = citasAdapter


        // Configurar el calendario
        calendarView = findViewById(R.id.calendarView)

        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {

            override fun onClick(calendarDay: CalendarDay) {
                val clickedDayCalendar = calendarDay.calendar

                // Guardar la fecha seleccionada
                calendarView.setDate(clickedDayCalendar)

                // Convertir la fecha seleccionada a "yyyy-MM-dd"
                val selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(clickedDayCalendar.time)

                // Consultar citas para la fecha seleccionada
                lifecycleScope.launch {

                    citaDao.getCitasByDate(selectedDate, mascotaId).collect { citas ->
                        citasAdapter.submitList(citas)
                    }
                }
            }
        })

        //obtener nombre de la mascota
        val nombreMascota = intent.getStringExtra("nombre_mascota")



        // Botón para agregar nuevas citas
        val btnAgregarCita: FloatingActionButton = findViewById(R.id.addCitasButton)
        btnAgregarCita.setOnClickListener {
            // Navegar a la actividad de registro de citas
            val intent = Intent(this, RegistroCitaActivity::class.java)
            intent.putExtra("mascota_id", mascotaId)
            intent.putExtra("nombre_mascota", nombreMascota)
            startActivity(intent)

        }

        // Cargar las citas iniciales
        //marcarFechasConEventos()

        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

        lifecycleScope.launch {
            citaDao.getProximasCitas(currentDate, mascotaId).collect { citas ->
                citasAdapter.submitList(citas)
            }
        }

        //Obtener todas las citas de la mascota
        lifecycleScope.launch {
            val citas = citaDao.getCitasByMascota(mascotaId)
            val events = citas.map {
                val parts = it.fechaHora.split(" ")[0].split("-")
                val calendar = Calendar.getInstance()
                calendar.set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
                EventDay(calendar, android.R.drawable.btn_star_big_on) // Usa un ícono para marcar el día
            }
            Log.d("CitasActivity", "Events Inicial: $events")
            calendarView.setEvents(events)
        }



            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }

    override fun onResume() {
        Log.d("CitasActivity", "onResume() called")
        super.onResume()
        actualizarCitas()
    }

    private fun actualizarCitas() {
        val citasAdapter = findViewById<RecyclerView>(R.id.rvProximasCitas).adapter as CitasAdapter
        val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
        Log.d("CitasActivity", "Updating citas for mascotaId: $mascotaId")
        lifecycleScope.launch {
            Log.d("CitasActivity", "Fetching citas for mascotaId: $mascotaId")
            citaDao.getProximasCitas(currentDate, mascotaId).collect { citas ->
                citasAdapter.submitList(citas)
            }


        }
        lifecycleScope.launch {
            val citas = citaDao.getCitasByMascota(mascotaId)
            val events = citas.map {
                val parts = it.fechaHora.split(" ")[0].split("-")
                val calendar = Calendar.getInstance()
                calendar.set(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
                EventDay(calendar, android.R.drawable.btn_star_big_on) // Usa un ícono para marcar el día
            }
            Log.d("CitasActivity", "Events: $events")
            calendarView.setEvents(events)
        }
    }

}