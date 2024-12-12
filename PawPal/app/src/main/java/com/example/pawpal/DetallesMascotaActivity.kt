package com.example.pawpal

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.data.database.PawPalDatabase
import com.example.pawpal.data.database.entities.Mascota
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class DetallesMascotaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalles_mascota)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configurar Toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        // Habilitar botón de "Back"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Configurar la acción del botón de "Back"
        toolbar.setNavigationOnClickListener {
            finish()
            val intent = Intent(this, MascotasActivity::class.java)
            startActivity(intent)

        }
        findViewById<CardView>(R.id.vacunasLinearLayout).setOnClickListener {
            Toast.makeText(this, "Citas con Veterinario", Toast.LENGTH_SHORT).show()
        }






        // Recuperar el ID de la mascota
        val mascotaId = intent.getIntExtra("mascota_id", -1)

        if (mascotaId != -1) {
            // Consultar la base de datos para obtener los detalles de la mascota
            lifecycleScope.launch {
                val mascota = withContext(Dispatchers.IO) {
                    PawPalDatabase.getDatabase(this@DetallesMascotaActivity)
                        .mascotaDao()
                        .getMascotaById(mascotaId)
                }
                toolbar.title = mascota?.nombre ?: "Mascota"
                mascota?.let {
                    mostrarDetallesMascota(it)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detalle_mascota, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_editar -> {
                // Navegar a la pantalla de edición

                return true
            }
            R.id.menu_eliminar -> {
                // Confirmar y eliminar la mascota
                mostrarConfirmacionEliminar()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun mostrarConfirmacionEliminar() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Eliminar Mascota")
        builder.setMessage("¿Estás seguro de que deseas eliminar esta mascota? Esta acción no se puede deshacer.")

        builder.setPositiveButton("Eliminar") { dialog, _ ->
            // Aquí se ejecuta la acción de eliminar
            eliminarMascota()
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            // Simplemente cierra el diálogo
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun eliminarMascota() {
        // Recupera el ID de la mascota
        val mascotaId = intent.getIntExtra("mascota_id", -1)

        if (mascotaId != -1) {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    PawPalDatabase.getDatabase(this@DetallesMascotaActivity)
                        .mascotaDao()
                        .deleteMascotaById(mascotaId) // Implementa este método en tu DAO
                }
                runOnUiThread {
                    Toast.makeText(this@DetallesMascotaActivity, "Mascota eliminada correctamente", Toast.LENGTH_SHORT).show()

                    // Finaliza la actividad
                    finish()
                    val intent = Intent(this@DetallesMascotaActivity, MascotasActivity::class.java)
                    startActivity(intent)
                }
            }
        } else {
            Toast.makeText(this, "Error al eliminar la mascota", Toast.LENGTH_SHORT).show()
        }
    }




    private fun mostrarDetallesMascota(mascota: Mascota) {
        // Mostrar los datos de la mascota en la UI
        val tipoTextView: TextView = findViewById(R.id.tipoMascota)
        val razaTextView: TextView = findViewById(R.id.razaMascota)
        val edadTextView: TextView = findViewById(R.id.edadMascota)
        val pesoTextView: TextView = findViewById(R.id.pesoMascota)
        val imagenMascota: ImageView = findViewById(R.id.imagenMascota)

        val imgFile = File(mascota.foto)
        if (imgFile.exists()) {
            val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
            imagenMascota.setImageBitmap(myBitmap)
        } else {
            // Imagen predeterminada si no existe
            imagenMascota.setImageResource(R.drawable.ic_pet_placeholder)
        }

        tipoTextView.text = "Tipo: ${mascota.tipo}"
        razaTextView.text = "Raza: ${mascota.raza}"
        edadTextView.text = "Edad: ${mascota.edad} años"
        pesoTextView.text = "Peso: ${mascota.peso} kg"


    }
}