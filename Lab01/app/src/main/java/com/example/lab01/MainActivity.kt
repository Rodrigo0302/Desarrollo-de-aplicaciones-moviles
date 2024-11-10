package com.example.lab01
//Flores Estopier Rodrigo
//Grupo 7CV1
//Fecha 9/10/2024
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lab01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val n1= binding.Number1.text
        val n2= binding.Number2.text

        binding.buttonPlus.setOnClickListener{
            val sumResult= n1.toString().toDouble() + n2.toString().toDouble()
            binding.resultView.text = sumResult.toString()
        }

        binding.buttonMinus.setOnClickListener{
            val minResult= n1.toString().toDouble() - n2.toString().toDouble()
            binding.resultView.text = minResult.toString()
        }

        binding.buttonMultiply.setOnClickListener{
            val mulResult= n1.toString().toDouble() * n2.toString().toDouble()
            binding.resultView.text = mulResult.toString()
        }

        binding.buttonDivide.setOnClickListener{
            val divResult= n1.toString().toDouble() / n2.toString().toDouble()
            binding.resultView.text = divResult.toString()
        }
    }
}

//Realizado por FER