package com.example.hotelapp.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class Servicio(
    val id: Int? = null, // Puede ser null si es auto-generado
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val disponibilidad: Boolean = true,
    val categoria : String,
    val imagen_url: String
)
