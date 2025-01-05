package com.example.pawpal

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.data.database.PawPalDatabase
import com.example.pawpal.data.database.entities.Paseo
import kotlinx.coroutines.launch
import java.util.Calendar

class RegistroPaseoActivity : AppCompatActivity() {
    private var mascotaId = -1 // Reemplaza con el ID de la mascota actual
    private var fechaSeleccionada: String = ""
    private var etFechaPaseo: EditText? = null
    private var etTituloPaseo: EditText? = null
    private var etDescripcionPaseo: EditText? = null
    private var duracionPaseo: String = ""
    private var etKMPaseo: EditText? = null

    private lateinit var npHours: NumberPicker
    private lateinit var npMinutes: NumberPicker
    private lateinit var npSeconds: NumberPicker


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_paseo)

        // Configurar Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Habilitar botón de "Back"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Configurar la acción del botón de "Back"
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Vuelve a la actividad anterior
            finish()
        }

        // Obtener el ID de la mascota del intent
        mascotaId = intent.getIntExtra("mascota_id", -1)

        //Inicializar vistas
        etFechaPaseo = findViewById(R.id.RegFechaPaseoEditText)
        etTituloPaseo = findViewById(R.id.RegTituloEditText)
        etDescripcionPaseo = findViewById(R.id.RegDescripcionEditText)
        etKMPaseo = findViewById(R.id.RegKMEditText)
        npHours = findViewById(R.id.npHours)
        npMinutes = findViewById(R.id.npMinutes)
        npSeconds = findViewById(R.id.npSeconds)

        npHours.minValue = 0
        npHours.maxValue = 99
        npMinutes.minValue = 0
        npMinutes.maxValue = 59
        npSeconds.minValue = 0
        npSeconds.maxValue = 59

        // Configurar botón para seleccionar fecha
        etFechaPaseo?.setOnClickListener {
            mostrarDatePicker()
        }
        // Configurar botón de guardar
        val guardarButton = findViewById<Button>(R.id.regiserPaseoButton)
        guardarButton.setOnClickListener {
            guardarPaseo()
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun mostrarDatePicker() {
        val calendario = Calendar.getInstance()

        // Selector de fecha
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                fechaSeleccionada = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)

                etFechaPaseo?.setText(fechaSeleccionada)
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun guardarPaseo() {
        val titulo = etTituloPaseo?.text.toString()
        val descripcion = etDescripcionPaseo?.text.toString()
        val km = etKMPaseo?.text.toString()
        val hours = npHours.value
        val minutes = npMinutes.value
        val seconds = npSeconds.value
        duracionPaseo = String.format("%02d:%02d:%02d", hours, minutes, seconds)

        //Validar campos
        if (titulo.isBlank() || descripcion.isBlank() || km.isBlank() || fechaSeleccionada.isBlank() || duracionPaseo.isBlank()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }
        val db = PawPalDatabase.getDatabase(this)
        val paseoDao = db.paseoDao()


        val nuevoPaseo= Paseo(
            titulo = titulo,
            descripcion = descripcion,
            kmRecorridos = km.toDouble(),
            fecha = fechaSeleccionada,
            duracion = duracionPaseo,
            mascotaId = mascotaId
        )

        lifecycleScope.launch {
            paseoDao.insert(nuevoPaseo)
                Toast.makeText(
                    this@RegistroPaseoActivity,
                    "Paseo registradó exitosamente",
                    Toast.LENGTH_SHORT
                ).show()

            finish()
        }
    }
}