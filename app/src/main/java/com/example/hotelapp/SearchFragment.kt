package com.example.hotelapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.adapters.MultiTypeAdapter
import com.example.hotelapp.dataclass.Producto
import com.example.hotelapp.dataclass.Servicio
import com.google.android.material.tabs.TabLayout
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

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MultiTypeAdapter
    private lateinit var searchEditText: TextInputEditText
    private lateinit var tabLayout: TabLayout

    private val items = mutableListOf<Any>() // Lista original
    private val filteredItems = mutableListOf<Any>() // Lista filtrada
    private var activeTab = "Todos" // Tab activa: "Todos", "Servicios", "Productos"

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
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Configurar campo de búsqueda
        searchEditText = view.findViewById(R.id.searchEditText)

        // Configurar TabLayout
        tabLayout = view.findViewById(R.id.tabLayout)

        // Inicializar el adaptador con la lista filtrada
        adapter = MultiTypeAdapter(filteredItems, supabaseClient.storage)
        recyclerView.adapter = adapter

        // Llenar la lista de productos y servicios
        cargarProductos()

        // Escuchar cambios en el texto del campo de búsqueda
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filtrarLista(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Configurar listener para el TabLayout
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                activeTab = tab?.text.toString()
                filtrarLista(searchEditText.text.toString())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun cargarProductos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Consultar productos desde Supabase
                val responseProductos = supabaseClient.from("productos")
                    .select()
                    .decodeList<Producto>()

                // Consultar servicios desde Supabase
                val responseServicios = supabaseClient.from("servicios")
                    .select()
                    .decodeList<Servicio>()

                // Actualizar la lista en el hilo principal
                withContext(Dispatchers.Main) {
                    items.clear()
                    items.addAll(responseProductos)
                    items.addAll(responseServicios)

                    // Mostrar todos los elementos inicialmente
                    filteredItems.clear()
                    filteredItems.addAll(items)
                    adapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun filtrarLista(query: String) {
        val lowerCaseQuery = query.lowercase()

        // Filtrar los elementos por texto y pestaña activa
        filteredItems.clear()
        filteredItems.addAll(items.filter {
            val matchesText = when (it) {
                is Producto -> it.nombre.lowercase().contains(lowerCaseQuery) || it.descripcion.lowercase().contains(lowerCaseQuery)
                is Servicio -> it.nombre.lowercase().contains(lowerCaseQuery) || it.descripcion.lowercase().contains(lowerCaseQuery)
                else -> false
            }

            val matchesTab = when (activeTab) {
                "Todos" -> true
                "Servicios" -> it is Servicio
                "Productos" -> it is Producto
                else -> false
            }

            matchesText && matchesTab
        })

        // Actualizar el adaptador
        adapter.notifyDataSetChanged()
    }
}
