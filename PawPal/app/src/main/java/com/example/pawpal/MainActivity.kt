package com.example.pawpal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.data.database.PawPalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val registerTextView = findViewById<TextView>(R.id.registerTextView)

        // Lógica para el botón de inicio de sesión
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                verificarUsuario(email, password)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el enlace de registro
        registerTextView.setOnClickListener {
            val intent = Intent(this, RegistroUsuarioActivity::class.java)
            startActivity(intent)

        }
    }

    private fun verificarUsuario(correo: String, contrasena: String) {
        val db = PawPalDatabase.getDatabase(this)
        val usuarioDao = db.usuarioDao()

        // Realizamos la consulta en un hilo secundario
        lifecycleScope.launch {
            val usuario = withContext(Dispatchers.IO) {
                usuarioDao.getUsuarioByCorreoAndContrasena(correo, contrasena)
            }

            if (usuario != null) {
                // Usuario encontrado, continuar a la siguiente actividad
                Toast.makeText(this@MainActivity, "Bienvenido, ${usuario.nombre}!", Toast.LENGTH_SHORT).show()

                val sessionManager = SessionManager(this@MainActivity)
                sessionManager.saveUserEmail(correo)

                // Navegar a la actividad de visualización de mascotas
                val intent = Intent(this@MainActivity, MascotasActivity::class.java)
                startActivity(intent)
                finish() // Cierra la actividad de inicio de sesión
            } else {
                // Usuario no encontrado o contraseña incorrecta
                Toast.makeText(this@MainActivity, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}