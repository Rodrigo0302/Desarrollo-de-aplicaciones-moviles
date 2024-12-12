package com.example.pawpal

import android.annotation.SuppressLint

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.data.database.PawPalDatabase
import com.example.pawpal.data.database.entities.Usuario
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch

import java.util.Calendar

import java.util.TimeZone // For TimeZone.getDefault()

class RegistroUsuarioActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_usuario)

        // Configurar Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Habilitar botón de "Back"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Configurar la acción del botón de "Back"
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Vuelve a la actividad anterior
        }


        val emailEditText = findViewById<EditText>(R.id.RegEmailEditText)
        val nombreEditText = findViewById<EditText>(R.id.RegNameEditText)
        val passwordEditText = findViewById<EditText>(R.id.RegPasswordEditText)
        val confirmPasswordEditText = findViewById<EditText>(R.id.RegPasswordRepeatEditText)
        val fechaNacimientoEditText = findViewById<EditText>(R.id.RegFechaNacEditText)
        val registerButton = findViewById<Button>(R.id.regiserUserButton)


        // Selección de fecha de nacimiento
        fechaNacimientoEditText.setOnClickListener {

            // Obtener la fecha actual en milisegundos UTC
            val today = MaterialDatePicker.todayInUtcMilliseconds()

            // Configurar restricciones del calendario con un validador para deshabilitar fechas futuras
            val constraintsBuilder = CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.before(today))

             // Mostrar un DatePicker
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Elige tu fecha de nacimiento")
                .setSelection(today) // Optional: Set initial selection
                .setCalendarConstraints(constraintsBuilder.build())
                .build()
            datePicker.show(supportFragmentManager, "datePicker")
            datePicker.addOnPositiveButtonClickListener { selection ->

                val selectedDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                selectedDate.timeInMillis = selection
                // 3. Adjust month value for display
                val year = selectedDate.get(Calendar.YEAR)
                val month = selectedDate.get(Calendar.MONTH) + 1
                val day = selectedDate.get(Calendar.DAY_OF_MONTH)
                //Toast.makeText(this, "Fecha seleccionada: $day/$month/$year", Toast.LENGTH_SHORT).show()
                fechaNacimientoEditText.setText("$day/$month/$year")

            }
        }

        // Acción del botón de registrarse
        registerButton.setOnClickListener {
            val correo = emailEditText.text.toString().trim()
            val nombre = nombreEditText.text.toString().trim()
            val fechaNacimiento = fechaNacimientoEditText.text.toString().trim()
            val contrasena = passwordEditText.text.toString().trim()
            val repetirContrasena = confirmPasswordEditText.text.toString().trim()

            if (validarCampos(correo, nombre, fechaNacimiento, contrasena, repetirContrasena)) {
                guardarUsuario(correo, nombre, fechaNacimiento, contrasena)
            }
        }
    }

    private fun validarCampos(
        correo: String, nombre: String, fechaNacimiento: String,
        contrasena: String, repetirContrasena: String
    ): Boolean {
        if (correo.isEmpty() || nombre.isEmpty() || fechaNacimiento.isEmpty() ||
            contrasena.isEmpty() || repetirContrasena.isEmpty()
        ) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return false
        }

        if (contrasena != repetirContrasena) {
            Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun guardarUsuario(correo: String, nombre: String, fechaNacimiento: String, contrasena: String) {
        val db = PawPalDatabase.getDatabase(this)
        val usuarioDao = db.usuarioDao()

        lifecycleScope.launch {
                val isEmailRegistered = usuarioDao.isEmailRegistered(correo)
                if (isEmailRegistered) {
                        Toast.makeText(
                            this@RegistroUsuarioActivity,
                            "El correo ya está registrado",
                            Toast.LENGTH_SHORT
                        ).show()
                } else {
                    usuarioDao.insert(Usuario(correo, nombre, fechaNacimiento, contrasena))
                    Toast.makeText(
                        this@RegistroUsuarioActivity,
                        "Usuario registrado exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()


                }

        }
    }
}
