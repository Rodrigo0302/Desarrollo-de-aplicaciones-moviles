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
import com.example.pawpal.data.database.entities.Alimento
import kotlinx.coroutines.launch
import java.util.Calendar

class RegistroAlimentoActivity : AppCompatActivity() {

    private var mascotaId = -1 // Reemplaza con el ID de la mascota actual
    private var fechaSeleccionada: String = ""
    private var etFechaAlimento: EditText? = null
    private var etNombreAlimento: EditText? = null
    private var etCantidadAlimento: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_alimento)

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
        etFechaAlimento = findViewById(R.id.RegFechaAlimentoEditText)
        etNombreAlimento = findViewById(R.id.RegNombreAlimentoEditText)
        etCantidadAlimento = findViewById(R.id.RegCantidadAlimentoEditText)

        val guardarButton = findViewById<Button>(R.id.regiserAlimentoButton)

        // Configurar botón para seleccionar fecha y hora
        etFechaAlimento?.setOnClickListener {
            mostrarDatePicker()
        }

        // Configurar botón de guardar
        guardarButton.setOnClickListener {
            guardarAlimento()
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

                etFechaAlimento?.setText(fechaSeleccionada)
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun guardarAlimento() {
        val nombre = etNombreAlimento?.text.toString()
        val cantidad = etCantidadAlimento?.text.toString()

        //Validar campos
        if (nombre.isBlank() || cantidad.isBlank() ||  fechaSeleccionada.isBlank()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }
        val db = PawPalDatabase.getDatabase(this)
        val alimentoDao = db.alimentoDao()

        //Pasar cantidad a double
        val cantidadDouble = cantidad.toDouble()


        val nuevoAlimento= Alimento(
            nombre = nombre,
            cantidad = cantidadDouble,
            fecha = fechaSeleccionada,
            mascotaId = mascotaId
        )

        lifecycleScope.launch {
            alimentoDao.insert(nuevoAlimento)
            Toast.makeText(
                this@RegistroAlimentoActivity,
                "Alimento registradó exitosamente",
                Toast.LENGTH_SHORT
            ).show()

            finish()
        }
    }
}