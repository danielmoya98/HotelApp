package com.example.hotelapp.dataclass
import kotlinx.serialization.Serializable

@Serializable
data class Pedido(
    val usuario_id: Int,
    val producto_id: Int,
    val estado: String
)
