package com.example.hotelapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class RegistrarActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        // Obtener referencia del botón
        val btnCreateAccount: MaterialButton = findViewById(R.id.btnCreateAccount)

        // Configurar el listener para el botón
        btnCreateAccount.setOnClickListener {
            // Mostrar un mensaje Toast
            Toast.makeText(this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()

            // Navegar a la nueva actividad
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}