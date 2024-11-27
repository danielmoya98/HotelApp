import kotlinx.serialization.Serializable

@Serializable
data class Producto(
    val id: Int? = null, // Puede ser null si es auto-generado en la base de datos
    val nombre: String,
    val descripcion: String,
    val precio: Double,
    val stock: Int,
    val imagen_url: String,
    val activo: Boolean = true,

)
