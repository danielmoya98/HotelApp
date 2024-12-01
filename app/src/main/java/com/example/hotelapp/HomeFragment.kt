package com.example.hotelapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.adapters.Destination
import com.example.hotelapp.adapters.DestinationAdapter
import com.example.hotelapp.adapters.Destinations
import com.example.hotelapp.adapters.DestinationsAdapter
import com.example.hotelapp.adapters.Producto
import com.example.hotelapp.adapters.ProductoAdapter
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//val supabase = createSupabaseClient(
//    supabaseUrl = "https://xjxpeyqzvalgoursiqxb.supabase.co",
//    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhqeHBleXF6dmFsZ291cnNpcXhiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzE0MzY0MTIsImV4cCI6MjA0NzAxMjQxMn0.xL5oNgjXi4LWBkmWezvtUYs6PMsPTe4lCviQkYUYPek"
//) {
//    install(Postgrest)
//}

class HomeFragment : Fragment() {

    private lateinit var recyclerViewCategorias: RecyclerView
    private lateinit var recyclerViewProductos: RecyclerView
    private lateinit var recyclerViewDestinations: RecyclerView
    private lateinit var recyclerViewDestinations2: RecyclerView

    private lateinit var productoAdapter: ProductoAdapter

//    // Estado Mutable para las categorías, inicializado vacío
//    private val _categoriaStateFlow = MutableStateFlow<List<Categoria>>(emptyList())
//    val categoriaStateFlow = _categoriaStateFlow.asStateFlow()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Configurar RecyclerView
//        recyclerViewCategorias = view.findViewById(R.id.recyclerCategorias)
//        recyclerViewCategorias.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recyclerViewCategorias = setupRecyclerView(view, R.id.recyclerCategorias)
        recyclerViewProductos = setupRecyclerView(view, R.id.recyclerProductos)
        recyclerViewDestinations = setupRecyclerView(view, R.id.recyclerDestinations)
        recyclerViewDestinations2 = setupRecyclerView(view, R.id.recyclerViewDestinationss)



        setupStaticDestinations()
        setupStaticProducts()

        return view
    }

    private fun setupRecyclerView(view: View, recyclerViewId: Int): RecyclerView {
        return view.findViewById<RecyclerView>(recyclerViewId).apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

//    private fun loadCategorias() {
//        lifecycleScope.launch {
//            val categorias = withContext(Dispatchers.IO) {
//                supabase.from("categorias")
//                    .select().decodeList<Categoria>()
//            }
//            _categoriaStateFlow.value = categorias
//        }
//    }

//    private fun observeCategorias() {
//        lifecycleScope.launch {
//            categoriaStateFlow.collect { categorias ->
//                categoriaAdapter.updateCategorias(categorias)
//            }
//        }
//    }

    private fun setupStaticDestinations() {
        val destinationsList = listOf(
            Destination("London", R.drawable.ft),
            Destination("Spain", R.drawable.ft1),
            Destination("Greece", R.drawable.ft2)
        )
        recyclerViewDestinations.adapter = DestinationAdapter(destinationsList)

        val destinationsList2 = listOf(
            Destinations(R.drawable.ft, "Roma", "Extraordinary five-star outdoor activities"),
            Destinations(R.drawable.ft1, "Paris", "Iconic views and romantic getaways")
        )
        recyclerViewDestinations2.adapter = DestinationsAdapter(destinationsList2)
    }

    private fun setupStaticProducts() {
        val productoList = listOf(
            Producto("Pizza Margherita", "Pizza con tomate, queso mozzarella y albahaca.", R.drawable.sauna),
            Producto("Hamburguesa", "Hamburguesa clásica con carne, queso y lechuga.", R.drawable.sauna),
            Producto("Ensalada César", "Ensalada con lechuga, crutones y aderezo César.", R.drawable.sauna),
            Producto("Tacos", "Tacos con carne, guacamole y salsa picante.", R.drawable.sauna)
        )
        productoAdapter = ProductoAdapter(productoList)
        recyclerViewProductos.adapter = productoAdapter
    }
}
