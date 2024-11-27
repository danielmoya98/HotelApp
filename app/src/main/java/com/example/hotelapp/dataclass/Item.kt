package com.example.hotelapp.dataclass

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val id: Int? = null,
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val imagen_url: String
)

@Serializable
data class Producto(
    val stock: Int,
    val item: Item // Composición: Producto contiene un Item
)

@Serializable
data class Servicio(
    val disponibilidad: Boolean,
    val item: Item // Composición: Servicio contiene un Item
)
