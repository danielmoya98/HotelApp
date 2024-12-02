package com.example.hotelapp

import android.content.Intent
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
import com.example.hotelapp.adapters.HomeProductAdapter
import com.example.hotelapp.adapters.HomeServiceAdapter
import com.example.hotelapp.dataclass.Producto
import com.example.hotelapp.dataclass.Servicio
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val supabase = createSupabaseClient(
    supabaseUrl = "https://xjxpeyqzvalgoursiqxb.supabase.co",
    supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhqeHBleXF6dmFsZ291cnNpcXhiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzE0MzY0MTIsImV4cCI6MjA0NzAxMjQxMn0.xL5oNgjXi4LWBkmWezvtUYs6PMsPTe4lCviQkYUYPek"
) {
    install(Postgrest)
}

class HomeFragment : Fragment() {

    private lateinit var recyclerViewDestinations: RecyclerView
    private lateinit var recyclerViewServicios: RecyclerView
    private lateinit var recyclerViewProductos: RecyclerView
    private lateinit var adapter: HomeProductAdapter
    private lateinit var serviciosAdapter: HomeServiceAdapter
    private val productos = mutableListOf<Producto>()
    private val servicios = mutableListOf<Servicio>()

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
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Configuramos el RecyclerView
        recyclerViewProductos = view.findViewById(R.id.recyclerProductos)
        recyclerViewProductos.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)

        // Creamos el adaptador para los productos
        adapter = HomeProductAdapter(productos, supabaseClient.storage) { producto ->
            val intent = Intent(requireContext(), DetailsActivity2::class.java)
            intent.putExtra("nombre", producto.nombre)
            intent.putExtra("descripcion", producto.descripcion)
            startActivity(intent)
        }
        recyclerViewProductos.adapter = adapter

        // Configuramos el RecyclerView para servicios
        recyclerViewServicios = view.findViewById(R.id.recyclerServicios)
        recyclerViewServicios.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        serviciosAdapter = HomeServiceAdapter(servicios, supabaseClient.storage) { servicio ->
            val intent = Intent(requireContext(), DetailsActivity2::class.java)
            intent.putExtra("id", servicio.id)
            intent.putExtra("nombre", servicio.nombre)
            intent.putExtra("descripcion", servicio.descripcion)
            intent.putExtra("foto", servicio.imagen_url)
            intent.putExtra("precio", servicio.precio)
            intent.putExtra("categoria", servicio.categoria)
            startActivity(intent)
        }
        recyclerViewServicios.adapter = serviciosAdapter

        // Cargamos los productos desde Supabase
        cargarProductos()
        cargarServicios()
        recyclerViewDestinations = setupRecyclerView(view, R.id.recyclerDestinations)

        setupStaticDestinations()

        return view
    }

    private fun setupRecyclerView(view: View, recyclerViewId: Int): RecyclerView {
        return view.findViewById<RecyclerView>(recyclerViewId).apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupStaticDestinations() {
        val destinationsList = listOf(
            Destination("London", R.drawable.ft),
            Destination("Spain", R.drawable.ft1),
            Destination("Greece", R.drawable.ft2)
        )
        recyclerViewDestinations.adapter = DestinationAdapter(destinationsList)
    }

    private fun cargarProductos() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Consultamos los productos desde Supabase
                val response = supabaseClient.from("productos")
                    .select()
                    .decodeList<Producto>()

                // Actualizamos la lista en el hilo principal
                withContext(Dispatchers.Main) {
                    productos.clear()
                    productos.addAll(response)
                    adapter.notifyDataSetChanged() // Notificamos al adaptador que los datos han cambiado
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun cargarServicios() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Consultamos los servicios desde Supabase
                val response = supabaseClient.from("servicios")
                    .select()
                    .decodeList<Servicio>()

                // Actualizamos la lista en el hilo principal
                withContext(Dispatchers.Main) {
                    servicios.clear()
                    servicios.addAll(response)
                    serviciosAdapter.notifyDataSetChanged() // Notificamos al adaptador que los datos han cambiado
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
