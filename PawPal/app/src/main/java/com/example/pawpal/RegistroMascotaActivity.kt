package com.example.pawpal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.data.database.PawPalDatabase
import com.example.pawpal.data.database.entities.Mascota
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


private var fotoRuta: String? = null // Almacena la ruta de la imagen seleccionada

class RegistroMascotaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_mascota)

        // Configurar Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Habilitar botón de "Back"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        val sessionManager = SessionManager(this)
        val userEmail = sessionManager.getUserEmail()
        // Configurar la acción del botón de "Back"
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed() // Vuelve a la actividad anterior
        }

        // Inicializar vistas
        var ivFotoMascota = findViewById<ImageView>(R.id.ivFotoMascota)

        var spTipoMascota = findViewById<Spinner>(R.id.spTipoMascota)

        var btnRegistrarMascota = findViewById<Button>(R.id.btnRegistrarMascota)

        // Crear un ArrayAdapter con los elementos del spinner
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_items,
            android.R.layout.simple_spinner_item
        )

        // Aplicar el estilo personalizado para el dropdown
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        // Asignar el adaptador al Spinner
        spTipoMascota.adapter = adapter



        // Selección de foto
        ivFotoMascota.setOnClickListener { seleccionarFoto() }

        // Registro de mascota
        btnRegistrarMascota.setOnClickListener { registrarMascota(userEmail) }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    var imgUri: Uri? = null

    private fun seleccionarFoto() {
        // Abrir selector de imágenes
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            imgUri = data.data
            var ivFotoMascota = findViewById<ImageView>(R.id.ivFotoMascota)
            var fotoRutaCopia = copyImageToAppStorage(imgUri!!)
            //Toast.makeText(this, "Imagen seleccionada${fotoRutaCopia}", Toast.LENGTH_SHORT).show()
            ivFotoMascota.setImageURI(imgUri)

            // Obtener ruta del archivo
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(imgUri!!, filePathColumn, null, null, null)
            cursor?.moveToFirst()
            val columnIndex = cursor!!.getColumnIndex(filePathColumn[0])
            fotoRuta = fotoRutaCopia
            cursor.close()
        }
    }

    private fun registrarMascota(userEmail: String?) {
        var etNombreMascota = findViewById<EditText>(R.id.etNombreMascota)
        var etRaza = findViewById<EditText>(R.id.etRaza)
        var etEdad = findViewById<EditText>(R.id.etEdad)
        var etPeso = findViewById<EditText>(R.id.etPeso)
        var spTipoMascota = findViewById<Spinner>(R.id.spTipoMascota)

        val nombre = etNombreMascota.text.toString()
        val tipo = spTipoMascota.selectedItem.toString()
        val raza = etRaza.text.toString()
        val edad = etEdad.text.toString().toIntOrNull()
        val peso = etPeso.text.toString().toDoubleOrNull()

        if (nombre.isEmpty() || fotoRuta.isNullOrEmpty() || edad == null || peso == null) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }
        //fotoRuta = imgUri.toString()

        // Guardar mascota en la base de datos (Room)
        lifecycleScope.launch {
            val mascota = Mascota(
                nombre = nombre,
                tipo = tipo,
                raza = raza,
                edad = edad,
                peso = peso,
                foto = fotoRuta!!,
                usuarioCorreo = userEmail.toString()
            )
            PawPalDatabase.getDatabase(applicationContext).mascotaDao().insert(mascota)
            Toast.makeText(this@RegistroMascotaActivity, "Mascota registrada", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@RegistroMascotaActivity, MascotasActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun copyImageToAppStorage(uri: Uri): String? {
        try {
            val inputStream = contentResolver.openInputStream(uri)
            val name = "copied_image_${System.currentTimeMillis()}.jpg"
            val file = File(getExternalFilesDir(null), name) // Guarda en almacenamiento accesible
            val outputStream = FileOutputStream(file)

            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            return file.absolutePath // Retorna la nueva ruta
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}