package com.example.hotelapp.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class Reserva(
    val usuario_id: Int,
    val servicio_id: Int,
    val estado: String
)
