package com.example.hotelapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hotelapp.dataclass.Notificacion
import com.example.hotelapp.dataclass.Pedido
import com.example.hotelapp.dataclass.Reserva
import com.example.hotelapp.utils.UserSession
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
class DetailsActivity2 : AppCompatActivity() {

    private lateinit var tvServicioNombre: TextView
    private lateinit var tvServicioDescripcion: TextView
    private lateinit var tvCategoria: TextView
    private lateinit var tvPrecio: TextView
    private lateinit var imgServicio: ImageView
    private lateinit var btnFinalizarCompra: Button

    private val supabase = createSupabaseClient(
        supabaseUrl = "https://xjxpeyqzvalgoursiqxb.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhqeHBleXF6dmFsZ291cnNpcXhiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzE0MzY0MTIsImV4cCI6MjA0NzAxMjQxMn0.xL5oNgjXi4LWBkmWezvtUYs6PMsPTe4lCviQkYUYPek"
    ) {
        install(Postgrest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details2)

        // Vincular vistas con IDs del XML
        tvServicioNombre = findViewById(R.id.tvServicioNombre)
        tvServicioDescripcion = findViewById(R.id.tvServicioDescripcion)
        tvCategoria = findViewById(R.id.tvCategoria)
        tvPrecio = findViewById(R.id.tvPrecio)
        imgServicio = findViewById(R.id.imgServicio)
        btnFinalizarCompra = findViewById(R.id.btnFinalizarCompra)

        // Recuperar datos enviados a través del Intent
        val userId = UserSession.userId
        val servicioId = intent.getIntExtra("id", -1)
        val nombre = intent.getStringExtra("nombre")
        val descripcion = intent.getStringExtra("descripcion")
        val imagenUrl = intent.getStringExtra("foto")
        val precio = intent.getDoubleExtra("precio", 0.0)
        val categoria = intent.getStringExtra("categoria")

        // Mostrar datos en las vistas
        tvServicioNombre.text = nombre
        tvServicioDescripcion.text = descripcion
        tvCategoria.text = "Categoría: $categoria"
        tvPrecio.text = "Precio: $${precio}"

        // Cargar imagen desde URL usando Glide
        if (!imagenUrl.isNullOrEmpty()) {
            Glide.with(this)
                .load(imagenUrl)
                .into(imgServicio)
        }

        // Configurar funcionalidad del botón "Finalizar Compra"
        btnFinalizarCompra.setOnClickListener {
            if (userId != null && servicioId != -1) {
                finalizarCompra(userId, servicioId)
            } else {
                Toast.makeText(this, "Error: datos incompletos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun finalizarCompra(userId: Int, servicioId: Int) {
        // Verificar la categoría para determinar la acción
        val categoria = tvCategoria.text.toString().replace("Categoría: ", "")

        if (categoria == "servicio" ) {
            // Crear reserva para servicios
            val reserva = Reserva(
                usuario_id = userId,
                servicio_id = servicioId,
                estado = "pendiente"
            )

            // Enviar datos al servidor para reservas
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = supabase.postgrest["reservas"].insert(reserva)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@DetailsActivity2, "Reserva realizada con éxito", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@DetailsActivity2, "Error al conectar con el servidor: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else if (categoria == "producto") {
            // Crear pedido para productos
            val pedido = Pedido(
                usuario_id = userId,
                producto_id = servicioId,
                estado = "pendiente"
            )

            // Enviar datos al servidor para pedidos
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = supabase.postgrest["pedidos"].insert(pedido)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@DetailsActivity2, "Pedido realizado con éxito", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("DetailsActivity2", "Error al conectar con el servidor", e)
                        Toast.makeText(this@DetailsActivity2, "Error al conectar con el servidor: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            Toast.makeText(this, "Categoría no válida", Toast.LENGTH_SHORT).show()
        }
    }

}

