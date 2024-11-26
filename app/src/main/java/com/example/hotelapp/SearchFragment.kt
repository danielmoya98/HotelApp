package com.example.hotelapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.adapters.Item
import com.example.hotelapp.adapters.ProductAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText

class SearchFragment : Fragment() {

    private lateinit var searchEditText: TextInputEditText
    private lateinit var tabLayout: TabLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ProductAdapter

    private val originalItems = listOf(
        // Servicios
        Item("Sauna", "Servicios", "Relájate en nuestro sauna atemperado.", 200.0),
        Item("Masaje Relax", "Servicios", "Masaje de 60 minutos para reducir estrés.", 150.0),
        Item("Tour Local", "Servicios", "Explora la ciudad con un guía.", 100.0),
        Item("Servicio de Habitación", "Servicios", "Comida y bebidas a tu puerta.", 20.0),
        Item("Transporte al Aeropuerto", "Servicios", "Traslado cómodo y rápido.", 50.0),

        // Productos
        Item("Shampoo Premium", "Productos", "Shampoo refrescante de alta calidad.", 10.0),
        Item("Albornoz", "Productos", "Albornoz suave y cómodo.", 40.0),
        Item("Set de Aseo", "Productos", "Cepillo, pasta dental y más.", 5.0),
        Item("Botella de Vino", "Productos", "Vino tinto de la región.", 25.0),
        Item("Mini Bar", "Productos", "Snacks y bebidas variadas.", 30.0)
    )

    private var filteredItems = mutableListOf<Item>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchEditText = view.findViewById(R.id.searchEditText)
        tabLayout = view.findViewById(R.id.tabLayout)
        recyclerView = view.findViewById(R.id.searchRecyclerView)

        // Configurar el RecyclerView y el Adapter
        adapter = ProductAdapter(filteredItems) { item ->
            openDetailsFragment(item)
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Inicialmente mostrar todos los elementos
        filteredItems.addAll(originalItems)
        adapter.notifyDataSetChanged()

        // Configurar el TabLayout para manejar el cambio de filtro
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                filterItems()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // Configurar el campo de búsqueda
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterItems()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun filterItems() {
        val query = searchEditText.text.toString().lowercase()
        val selectedTab = tabLayout.selectedTabPosition

        filteredItems.clear()
        filteredItems.addAll(originalItems.filter { item ->
            // Mostrar todos los elementos si la pestaña seleccionada es "Todos" (posición 0)
            (selectedTab == 0 || item.category == if (selectedTab == 1) "Servicios" else "Productos") &&
                    item.name.lowercase().contains(query)
        })
        adapter.notifyDataSetChanged()
    }

    private fun openDetailsFragment(item: Item) {
        val intent = Intent(requireContext(), DetailsActivity2::class.java).apply {
            putExtra("item", item) // Pasamos el objeto Item como Parcelable
        }
        startActivity(intent) // Iniciamos la nueva actividad
    }
}
