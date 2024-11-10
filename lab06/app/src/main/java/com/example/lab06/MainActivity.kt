package com.example.lab06
//Flores Estopier Rodrigo
//Grupo 7CV1
//Fecha 22/10/2024

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.lab06.databinding.ActivityMainBinding
import android.view.View

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

    fun display(view:View){
        binding.info.text = "Android Application Development, Android Security Essentials " +
                "and Monetize Android Applications"
    }

    fun display2(view:View){
        binding.info.text = "Informacion de examenes"
    }

    fun display3(view:View){
        binding.info.text = "Informacion de Freelance"
    }
}