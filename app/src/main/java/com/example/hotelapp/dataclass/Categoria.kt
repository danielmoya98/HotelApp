package com.example.hotelapp.dataclass
import kotlinx.serialization.Serializable
@Serializable
data class Categoria(
    val nombre_categoria: String,
    val descripcion: String
)
