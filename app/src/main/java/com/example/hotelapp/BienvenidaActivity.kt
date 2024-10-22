package com.example.hotelapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class BienvenidaActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bienvenida)

        val btnLogin = findViewById<MaterialButton>(R.id.btnLogin)
        val btnSignUp = findViewById<MaterialButton>(R.id.btnSignUp)

        btnLogin.setOnClickListener {
            val loginIntent = Intent(this, MainActivity::class.java)
            startActivity(loginIntent)
        }

        btnSignUp.setOnClickListener {
            val signUpIntent = Intent(this, RegistrarActivity::class.java)
            startActivity(signUpIntent)
        }
    }
}