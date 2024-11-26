package com.example.hotelapp.dataclass

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int,
    val nombre: String,
    val correo: String,
    @SerialName("foto_url") val fotoUrl: String? = null, // Cambia "fotoUrl" para que coincida con "foto_url",
    val contrasena: String,
    val telefono: String,
    val rol: String,
)
