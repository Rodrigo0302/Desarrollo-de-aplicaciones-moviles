package com.example.lab06

//Flores Estopier Rodrigo
//Grupo 7CV1
//Fecha 05/11/2024

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab06.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun mySchedule(view: View){
        var intent = Intent(this, Activity2::class.java)
        intent.putExtra("name",binding.nameId.text.toString())
        intent.putExtra("address",binding.addressId.text.toString())
        intent.putExtra("city",binding.cityId.text.toString())
        intent.putExtra("state",binding.stateId.text.toString())
        intent.putExtra("zipCode",binding.zipId.text.toString())
        intent.putExtra("country",binding.countryId.text.toString())
        startActivity(intent)
    }
}