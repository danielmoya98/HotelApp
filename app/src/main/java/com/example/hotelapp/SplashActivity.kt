package com.example.hotelapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Usar un Handler para hacer el retraso de 3 segundos
        Handler(Looper.getMainLooper()).postDelayed({
            // Iniciar la MainActivity después de 3 segundos
            val intent = Intent(this, BienvenidaActivity::class.java)
            startActivity(intent)
            finish() // Cierra la SplashActivity para que no se quede en el stack
        }, 3000) // 3000 milisegundos = 3 segundos
    }
}
