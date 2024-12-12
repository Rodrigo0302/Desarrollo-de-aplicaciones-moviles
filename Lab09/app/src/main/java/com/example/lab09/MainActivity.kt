package com.example.lab09
//Flores Estopier Rodrigo
//Grupo 7CV1
//Fecha 09/12/2024

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    lateinit var myToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val drawerLayout: DrawerLayout = findViewById(R.id.myDrawerLayout)
        val navView: NavigationView = findViewById(R.id.myDrawerView)

        myToggle = ActionBarDrawerToggle(this,drawerLayout,0,0)
        drawerLayout.addDrawerListener(myToggle)

        myToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sendId -> {
                    Snackbar.make(findViewById(R.id.sendId), "Your email has been sent successfully", Snackbar.LENGTH_SHORT).show()
                    true // Indicate that the item selection was handled
                }
                R.id.outboxId -> {
                    Snackbar.make(findViewById(R.id.outboxId), "This is your outbox folder", Snackbar.LENGTH_SHORT).show()
                    true
                }
                R.id.spamId -> {
                    Snackbar.make(findViewById(R.id.spamId), "This is your spam folder", Snackbar.LENGTH_SHORT).show()
                    true
                }
                R.id.calendarId -> {
                    Snackbar.make(findViewById(R.id.calendarId), "This is your calendar", Snackbar.LENGTH_SHORT).show()
                    true
                }
                R.id.contactsId -> {
                    startActivity(Intent(this, Contacts::class.java))
                    true
                }
                R.id.settingsId -> {
                    Snackbar.make(findViewById(R.id.settingsId), "This is your settings", Snackbar.LENGTH_SHORT).show()
                    true
                }
                else -> false // Handle other cases or return false if not handled
            }

        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.myDrawerLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(myToggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}