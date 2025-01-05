package com.example.pawpal

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.NotificationManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.pawpal.data.database.PawPalDatabase
import com.example.pawpal.data.database.entities.Cita
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit


import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.pawpal.utils.ReminderWorker

class RegistroCitaActivity : AppCompatActivity() {
    private var mascotaId = 1 // Reemplaza con el ID de la mascota actual
    private var fechaHoraSeleccionada: String = ""
    private lateinit var etFechaHoraCita: EditText
    private lateinit var etMotivoCita: EditText
    private var timeDelayinSeconds : Long = -1
    private lateinit var nombreMascota: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_cita)

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
        nombreMascota = intent.getStringExtra("nombre_mascota") ?: ""

        //Inicializar vistas
        etFechaHoraCita = findViewById(R.id.RegFechaCitaEditText)
        etMotivoCita = findViewById(R.id.RegAsuntoEditText)
        val guardarButton = findViewById<Button>(R.id.regiserCitaButton)

        // Configurar botón para seleccionar fecha y hora
        etFechaHoraCita.setOnClickListener {
            mostrarDateTimePicker()
        }

        // Configurar botón de guardar
        guardarButton.setOnClickListener {
            guardarCita()
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun mostrarDateTimePicker() {
        val calendario = Calendar.getInstance()

        // Selector de fecha
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val fechaSeleccionada = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)

                // Selector de hora
                val timePicker = TimePickerDialog(
                    this,
                    { _, hourOfDay, minute ->
                        val horaSeleccionada = String.format("%02d:%02d", hourOfDay, minute)
                        fechaHoraSeleccionada = "$fechaSeleccionada $horaSeleccionada"
                        etFechaHoraCita.setText(fechaHoraSeleccionada)
                        val calendar = Calendar.getInstance()
                        calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                        timeDelayinSeconds = (calendar.timeInMillis - System.currentTimeMillis()) / 1000L
                    },
                    calendario.get(Calendar.HOUR_OF_DAY),
                    calendario.get(Calendar.MINUTE),
                    true
                )
                timePicker.show()
            },
            calendario.get(Calendar.YEAR),
            calendario.get(Calendar.MONTH),
            calendario.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun guardarCita() {
        val asunto = etMotivoCita.text.toString()
        if (asunto.isBlank() || fechaHoraSeleccionada.isBlank()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        val db = PawPalDatabase.getDatabase(this)
        val citaDao = db.citaDao()


        val nuevaCita = Cita(
            asunto = asunto,
            fechaHora = fechaHoraSeleccionada,
            mascotaId = mascotaId
        )

        lifecycleScope.launch {
                citaDao.insert(nuevaCita)
                if(timeDelayinSeconds<0)
                {
                    Toast.makeText(
                        this@RegistroCitaActivity,
                        "La fecha y hora seleccionada ya ha pasado",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }else {
                    addReminder(nuevaCita)
                    Toast.makeText(
                        this@RegistroCitaActivity,
                        "Cita registrada exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            //finish()
        }
    }

    private fun addReminder(nuevaCita: Cita) {
        if (Build.VERSION.SDK_INT>=33 && !NotificationManagerCompat.from(this).areNotificationsEnabled()){
            Log.d("NotificationMas","Notifications Disabled")
            showNotificationPermissionDialog()
        }else{
            Log.d("NotificationMas","Notifications Enabled")
            programarRecordatorio(nuevaCita)
        }
    }

    private fun showNotificationPermissionDialog() {
        MaterialAlertDialogBuilder(this,com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setTitle("Permiso de Notificaciones")
            .setMessage("El permiso es necesario para programar recordatorios de citas")
            .setPositiveButton("OK"
            ) { _, _ ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    notificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            .setNegativeButton("Cancelar",null)
            .show()
    }

    private val notificationPermissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isGranted ->
        if(!isGranted){
            if(Build.VERSION.SDK_INT>=33){
                if(shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)){
                    showNotificationPermissionDialog()
                }else{
                    showSettingsDialog()
                }
            }
        }else{
            Toast.makeText(this,"Permiso Otorgado",Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSettingsDialog(){
        MaterialAlertDialogBuilder(this,com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setTitle("Permiso de Notificaciones")
            .setMessage("El permiso es necesario para programar recordatorios de citas")
            .setPositiveButton("OK"
            ) { _, _ ->
                val intent= Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data= Uri.parse("package:${this.applicationContext?.packageName}")
                startActivity(intent)
            }
            .setNegativeButton("Cancelar",null)
            .show()


    }

    private fun createWorkRequest(title:String,reminderType:String,delay:Long) {
        //Formatear titulo y mensaje
        val titulo="Recordatorio: $title"
        val descripcion="Tienes una cita con tu mascota $nombreMascota"

        val reminderWorkRequest= OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setInputData(workDataOf("Title" to titulo, "Message" to descripcion))
            .build()
        WorkManager.getInstance(this).enqueue(reminderWorkRequest)

        Log.d("NotificationMas","Work Request Creado")
        finish()
    }

    private fun programarRecordatorio(cita: Cita) {

        //Work Manager
        createWorkRequest(cita.asunto,"Alarma",timeDelayinSeconds)
        Toast.makeText(this,"Recordatorio programado",Toast.LENGTH_LONG).show()
        Log.d("NotificationMas","Tiempo de delay: $timeDelayinSeconds")
        Log.d("NotificationMas","Recordatorio programado")
        //finish()
    }
}