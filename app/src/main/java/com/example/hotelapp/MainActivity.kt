package com.example.hotelapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val signInButton = findViewById<MaterialButton>(R.id.signInButton)
        val textViewSignUp = findViewById<TextView>(R.id.textViewSignUp)

        signInButton.setOnClickListener {
            val mainIntent = Intent(this, HomeActivity::class.java)
            startActivity(mainIntent)
        }

        textViewSignUp.setOnClickListener {
            val signUpIntent = Intent(this, RegistrarActivity::class.java)
            startActivity(signUpIntent)
        }
    }
}