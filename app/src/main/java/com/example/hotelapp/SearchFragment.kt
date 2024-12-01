package com.example.hotelapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.adapters.ProductAdapter
import com.example.hotelapp.dataclass.Producto
import com.example.hotelapp.dataclass.Servicio
import com.google.android.material.textfield.TextInputEditText
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFragment : Fragment() {

    private lateinit var searchEditText: TextInputEditText
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter
    private val productos = mutableListOf<Producto>()
    private val servicios = mutableListOf<Producto>()

    // Crear cliente Supabase
    private val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://xjxpeyqzvalgoursiqxb.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhqeHBleXF6dmFsZ291cnNpcXhiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzE0MzY0MTIsImV4cCI6MjA0NzAxMjQxMn0.xL5oNgjXi4LWBkmWezvtUYs6PMsPTe4lCviQkYUYPek"
    ) {
        install(io.github.jan.supabase.postgrest.Postgrest)
        install(Storage)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView) // Asegúrate de usar el ID correcto
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductAdapter(productos, supabaseClient.storage)
        recyclerView.adapter = adapter

        // Llamar a cargarProductos para llenar la lista
        cargarProductos()

    }

    private fun cargarProductos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Consultar productos desde Supabase
                val response = supabaseClient.from("productos")
                    .select()
                    .decodeList<Producto>()

                // Actualizar la lista en el hilo principal
                withContext(Dispatchers.Main) {
                    productos.clear()
                    productos.addAll(response)
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
