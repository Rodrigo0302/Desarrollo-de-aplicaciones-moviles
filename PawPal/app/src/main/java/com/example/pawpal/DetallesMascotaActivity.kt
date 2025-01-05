package com.example.pawpal

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
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
    private var mascotaId: Int = -1
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
        // Citas con Veterinario
        findViewById<CardView>(R.id.citasLinearLayout).setOnClickListener {
            val intent = Intent(this, CitasActivity::class.java)
            intent.putExtra("mascota_id", mascotaId)
            intent.putExtra("nombre_mascota", toolbar.title)
            startActivity(intent)
        }
        // Alimentación
        findViewById<CardView>(R.id.alimentacionLinearLayout).setOnClickListener {
            val intent = Intent(this, AlimentacionActivity::class.java)
            intent.putExtra("mascota_id", mascotaId)
            //intent.putExtra("nombre_mascota", toolbar.title)
            startActivity(intent)
        }
        // Paseos
        findViewById<CardView>(R.id.paseosLinearLayout).setOnClickListener {
            val intent = Intent(this, PaseosActivity::class.java)
            intent.putExtra("mascota_id", mascotaId)
            //intent.putExtra("nombre_mascota", toolbar.title)
            startActivity(intent)
        }
        // Vacunas
        findViewById<CardView>(R.id.vacunasLinearLayout).setOnClickListener {
            val intent = Intent(this, VacunasActivity::class.java)
            intent.putExtra("mascota_id", mascotaId)
            //intent.putExtra("nombre_mascota", toolbar.title)
            startActivity(intent)
        }








        // Recuperar el ID de la mascota
        mascotaId = intent.getIntExtra("mascota_id", -1)

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

    private val editarMascotaLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if (result.resultCode == RESULT_OK){
            val updatedMascotaId = result.data?.getIntExtra("mascota_id", -1) ?: -1
            if (updatedMascotaId != -1) {
                // Actualizar detalles con los nuevos datos
                actualizarDetallesMascota(updatedMascotaId)
            }
        }

    }

    private fun actualizarDetallesMascota(mascotaId: Int) {
        lifecycleScope.launch {
            val mascota = withContext(Dispatchers.IO) {
                PawPalDatabase.getDatabase(this@DetallesMascotaActivity)
                    .mascotaDao()
                    .getMascotaById(mascotaId)
            }
            mascota?.let {
                mostrarDetallesMascota(it)
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
                val intent = Intent(this, EditarMascotaActivity::class.java)
                //Toast.makeText(this, "Mascota id de aca: ${mascotaId}", Toast.LENGTH_SHORT).show()
                intent.putExtra("mascota_id",mascotaId)
                editarMascotaLauncher.launch(intent)

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
        // Actualizar titulo del toolbar
        supportActionBar?.title = mascota.nombre


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