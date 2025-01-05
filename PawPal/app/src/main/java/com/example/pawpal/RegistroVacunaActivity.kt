package com.example.pawpal

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.data.database.PawPalDatabase
import com.example.pawpal.data.database.entities.Vacuna
import kotlinx.coroutines.launch
import java.util.Calendar

class RegistroVacunaActivity : AppCompatActivity() {

    private var mascotaId = -1 // Reemplaza con el ID de la mascota actual
    private var fechaSeleccionada: String = ""
    private var etFechaVacuna: EditText? = null
    private var etNombreVacuna: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_vacuna)

        // Obtener el ID de la mascota del intent
        mascotaId = intent.getIntExtra("mascota_id", -1)

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

        // Inicializar vistas
        etFechaVacuna = findViewById(R.id.RegFechaVacunaEditText)
        etNombreVacuna = findViewById(R.id.RegNombreVacunaEditText)

        val guardarButton = findViewById<Button>(R.id.registerVacunaButton)

        // Configurar botón para seleccionar fecha y hora
        etFechaVacuna?.setOnClickListener {
            mostrarDatePicker()
        }

        // Configurar botón de guardar
        guardarButton.setOnClickListener {
            guardarVacuna()
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

                etFechaVacuna?.setText(fechaSeleccionada)
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun guardarVacuna() {
        val nombre = etNombreVacuna?.text.toString()

        //Validar campos
        if (nombre.isBlank() ||  fechaSeleccionada.isBlank()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }
        val db = PawPalDatabase.getDatabase(this)
        val vacunaDao = db.vacunaDao()



        val nuevaVacuna= Vacuna(
            nombre = nombre,
            fechaAplicacion = fechaSeleccionada,
            mascotaId = mascotaId
        )

        lifecycleScope.launch {
            vacunaDao.insert(nuevaVacuna)
            Toast.makeText(
                this@RegistroVacunaActivity,
                "Vacuna registrada exitosamente",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}