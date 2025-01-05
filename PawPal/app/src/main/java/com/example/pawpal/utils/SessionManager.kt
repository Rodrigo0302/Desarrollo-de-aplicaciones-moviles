package com.example.pawpal.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    // Guardar correo del usuario en la sesión
    fun saveUserEmail(email: String) {
        editor.putString("USER_EMAIL", email)
        editor.apply()
    }

    // Obtener correo del usuario de la sesión
    fun getUserEmail(): String? {
        return sharedPreferences.getString("USER_EMAIL", null)
    }

    // Cerrar sesión
    fun clearSession() {
        editor.clear()
        editor.apply()
    }
}
