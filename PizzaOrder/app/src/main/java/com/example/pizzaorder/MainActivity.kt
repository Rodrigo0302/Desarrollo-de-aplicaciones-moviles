package com.example.pizzaorder
//Flores Estopier Rodrigo
//Grupo 7CV1
//Fecha 15/10/2024

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pizzaorder.databinding.ActivityMainBinding

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

fun onPlaceOrderButtonClicked(view:View){
    var pizzaSizePrice = 0.0
    var toppingsTotal = 0.0

    when (binding.grupoRadio.checkedRadioButtonId) {
        R.id.smallPizza -> pizzaSizePrice = 5.0
        R.id.mediumpizza -> pizzaSizePrice = 7.0
        R.id.largepizza -> pizzaSizePrice = 9.0
    }

    if (binding.onionsCheckBox.isChecked){ toppingsTotal += 1}
    if (binding.olivesCheckBox.isChecked){ toppingsTotal += 2}
    if (binding.tomatoesCheckBox.isChecked){ toppingsTotal += 3}

    val texto = "Total Order Price: $" + (pizzaSizePrice+toppingsTotal)
    binding.totalPrice.text = texto

    }
}

