package com.example.lab08
//Flores Estopier Rodrigo
//Grupo 7CV1
//Fecha 23/11/2024

import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Thanks : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_thanks)
        val name = intent.getStringExtra("name")
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val pizzSize = intent.getStringExtra("pizzSize")
        val pickupDate = intent.getStringExtra("pickupDate")
        val pickupTime = intent.getStringExtra("pickupTime")

        val nameText: TextView = findViewById(R.id.nameId)
        val phoneNumberText: TextView = findViewById(R.id.phoneId)
        val pizzSizeText: TextView = findViewById(R.id.sizeId)
        val pickupDateText: TextView = findViewById(R.id.dateId)
        val pickupTimeText: TextView = findViewById(R.id.timeId)

        nameText.text = name
        phoneNumberText.text = phoneNumber
        pizzSizeText.text = pizzSize
        pickupDateText.text = pickupDate
        pickupTimeText.text = pickupTime

        val sendButton: Button = findViewById(R.id.sendBtn)
        sendButton.setOnClickListener {
            val rating = findViewById<RatingBar>(R.id.myRatingBar).rating
            val ratingText = findViewById<TextView>(R.id.rateText)
            ratingText.text = "$rating"
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}