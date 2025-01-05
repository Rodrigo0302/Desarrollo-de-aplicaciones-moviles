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
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.data.database.PawPalDatabase
import com.example.pawpal.data.database.entities.Mascota
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class EditarMascotaActivity : AppCompatActivity() {

    private var mascotaId: Int = -1
    private var userEmail: String? = null
    var fotoRuta: String? = null // Almacena la ruta de la imagen seleccionada
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_mascota)

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
        var ivFotoMascota = findViewById<ImageView>(R.id.ivFotoMascota)

        // Selección de foto
        ivFotoMascota.setOnClickListener { seleccionarFoto() }

        var spTipoMascota = findViewById<Spinner>(R.id.spTipoMascota)

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

        //Configurar el boton para guardar los cambios
        val botonGuardar = findViewById<Button>(R.id.btnRegistrarMascota)
        botonGuardar.text = "Guardar"
        botonGuardar.setOnClickListener {
            guardarCambios()
        }

        //Obten el id del intent
        mascotaId = intent.getIntExtra("mascota_id", -1)
        //Toast.makeText(this, "Mascota id: $mascotaId", Toast.LENGTH_SHORT).show()
        if(mascotaId != -1){
            // Consultar la base de datos para obtener los detalles de la mascota
            lifecycleScope.launch {
                val mascota = withContext(Dispatchers.IO) {
                    PawPalDatabase.getDatabase(this@EditarMascotaActivity)
                        .mascotaDao()
                        .getMascotaById(mascotaId)
                }
                mascota?.let { cargarDatosMascota(it) }
            }


        }

    }
    private var imgUri: Uri? = null

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

    private fun cargarDatosMascota(mascota: Mascota) {
        // Aquí puedes cargar los datos de la mascota en los campos de texto
        findViewById<EditText>(R.id.etNombreMascota).setText(mascota.nombre)
        findViewById<EditText>(R.id.etRaza).setText(mascota.raza)
        findViewById<EditText>(R.id.etEdad).setText(mascota.edad.toString())
        findViewById<EditText>(R.id.etPeso).setText(mascota.peso.toString())

        // Configurar el Spinner con el tipo de mascota
        val spTipoMascota = findViewById<Spinner>(R.id.spTipoMascota)
        val adapter = spTipoMascota.adapter as ArrayAdapter<String>
        val position = adapter.getPosition(mascota.tipo)
        spTipoMascota.setSelection(position)

        // Configurar la imagen de la mascota
        val ivFotoMascota = findViewById<ImageView>(R.id.ivFotoMascota)
        ivFotoMascota.setImageURI(Uri.parse(mascota.foto))
        fotoRuta = mascota.foto

        userEmail = mascota.usuarioCorreo

    }

    private fun guardarCambios() {
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

        // Actualizar la mascota en la base de datos (Room)

        lifecycleScope.launch {
            val mascota = Mascota(
                id = mascotaId,
                nombre = nombre,
                tipo = tipo,
                raza = raza,
                edad = edad,
                peso = peso,
                foto = fotoRuta!!,
                usuarioCorreo = userEmail.toString()
            )
            PawPalDatabase.getDatabase(applicationContext).mascotaDao().update(mascota)
            runOnUiThread {
                Toast.makeText(this@EditarMascotaActivity, "Cambios guardados", Toast.LENGTH_SHORT).show()

                // Regresa a la actividad de detalles con el resultado
                val resultIntent = Intent()
                resultIntent.putExtra("mascota_id", mascotaId)
                setResult(RESULT_OK, resultIntent)
                finish()
            }
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