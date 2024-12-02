package com.example.hotelapp.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class Notificacion(
    val usuario_id: Int,
    val reserva_id: Int,
    val mensaje: String,
    val leido: Boolean = false,
    val fecha_envio: String? = null // Este valor puede ser autogenerado por la base de datos
)