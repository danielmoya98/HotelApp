package com.example.hotelapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class DetailsActivity2 : AppCompatActivity() {

    private lateinit var tvServicioNombre: TextView
    private lateinit var itemDescriptionTextView: TextView // ID coincide con el XML

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details2)

        tvServicioNombre = findViewById(R.id.tvServicioNombre)
        itemDescriptionTextView = findViewById(R.id.tvDescripcionCompleta)



    }
}
