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
import com.example.lab06.databinding.Activity2Binding

class Activity2 : AppCompatActivity() {
    private lateinit var binding: Activity2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = Activity2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.SnameId.text = intent.getStringExtra("name")
        binding.SaddressId.text = intent.getStringExtra("address")
        binding.ScityId.text = intent.getStringExtra("city")
        binding.SstateId.text = intent.getStringExtra("state")
        binding.SzipId.text = intent.getStringExtra("zipCode")
        binding.ScountryId.text = intent.getStringExtra("country")

    }
    fun accept(view:View){
        val agree = Intent(this, ThankYou::class.java)
        startActivity(agree)
    }
    fun edit(view:View){
        onBackPressedDispatcher.onBackPressed()
    }
}