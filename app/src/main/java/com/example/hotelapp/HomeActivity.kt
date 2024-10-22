package com.example.hotelapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.adapters.Categoria
import com.example.hotelapp.adapters.CategoriaAdapter
import com.example.hotelapp.adapters.Producto
import com.example.hotelapp.adapters.ProductoAdapter

class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoriaAdapter: CategoriaAdapter
    private lateinit var categoriaList: List<Categoria>
    private lateinit var recyclerView2: RecyclerView
    private lateinit var productoAdapter: ProductoAdapter
    private lateinit var productoList: List<Producto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Configuración del RecyclerView de categorías en horizontal
        recyclerView = findViewById(R.id.recyclerCategorias)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Configuración del RecyclerView de productos en horizontal
        recyclerView2 = findViewById(R.id.recyclerProductos)
        recyclerView2.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Lista de categorías
        categoriaList = listOf(
            Categoria("Comida y Bebidas", R.drawable.user),
            Categoria("Limpieza de Habitación", R.drawable.user),
            Categoria("Mantenimiento", R.drawable.user),
            Categoria("Servicios Especiales", R.drawable.user)
        )

        // Lista de productos
        productoList = listOf(
            Producto("Pizza Margherita", "Pizza con tomate, queso mozzarella y albahaca.", R.drawable.sauna),
            Producto("Hamburguesa", "Hamburguesa clásica con carne, queso y lechuga.", R.drawable.sauna),
            Producto("Ensalada César", "Ensalada con lechuga, crutones y aderezo César.", R.drawable.sauna),
            Producto("Tacos", "Tacos con carne, guacamole y salsa picante.", R.drawable.sauna)
        )

        // Adaptadores
        categoriaAdapter = CategoriaAdapter(categoriaList)
        recyclerView.adapter = categoriaAdapter

        productoAdapter = ProductoAdapter(productoList)
        recyclerView2.adapter = productoAdapter
    }
}