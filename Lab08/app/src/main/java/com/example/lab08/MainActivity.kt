package com.example.lab08

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

//Flores Estopier Rodrigo
//Grupo 7CV1
//Fecha 23/11/2024

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val slider: SeekBar = findViewById(R.id.myseekBar)
        val sliderValue: TextView = findViewById(R.id.myPizzaSize)

        val buttonDate: Button = findViewById(R.id.dateBtn)
        val dateText: TextView = findViewById(R.id.dateText)

        val timeButton: Button = findViewById(R.id.timeBtn)
        val timeText: TextView = findViewById(R.id.timeText)

        val scheduleButton: Button = findViewById(R.id.scheduleBtn)

        val myPhoneNumber: EditText = findViewById(R.id.myPhoneNumber)
        val name: EditText = findViewById(R.id.myFullName)


        val pizzaSizes = arrayListOf("Please Select", "Small", "Medium", "Large", "Extra-Large")

        slider.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sliderValue.text = pizzaSizes[progress]
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        }
        )

        buttonDate.setOnClickListener {
            val c = Calendar.getInstance()
            val day= c.get(Calendar.DAY_OF_MONTH)
            val month = c.get(Calendar.MONTH)
            val year = c.get(Calendar.YEAR)

            val datePicker = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{
                    DatePicker, Year, Month, Day ->
                dateText.text = "$Day/${Month+1}/$Year"
            }, year, month, day)
            datePicker.show()
        }

        timeButton.setOnClickListener {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)
            val timePicker = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{
                    TimePicker, Hour, Minute ->
                timeText.text = "$Hour:$Minute"
            }, hour, minute, true)
            timePicker.show()
        }

        scheduleButton.setOnClickListener {
            val intent = Intent(this, Thanks::class.java)
            intent.putExtra("name", name.text.toString())
            intent.putExtra("phoneNumber", myPhoneNumber.text.toString())
            intent.putExtra("pizzSize", sliderValue.text.toString())
            intent.putExtra("pickupDate", dateText.text.toString())
            intent.putExtra("pickupTime", timeText.text.toString())
            startActivity(intent)
        }

    }
}