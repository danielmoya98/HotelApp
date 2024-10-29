package com.example.hotelapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.adapters.Notification
import com.example.hotelapp.adapters.NotificationAdapter

class NotificationsFragment : Fragment() {

    private lateinit var adapter: NotificationAdapter
    private lateinit var notifications: List<Notification>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializar la lista de notificaciones
        notifications = listOf(
            Notification("Pedido #12345", "Enviado\nFecha estimada: 10/10/2023", "Pedido"),
            Notification("Oferta Especial", "20% Descuento\nVálido hasta: 16/10/2023", "Oferta"),
            Notification("Pedido #67890", "En camino\nFecha estimada: 12/10/2023", "Pedido"),
            Notification("Descuento en...", "Descuento\nVálido hasta: 20/10/2023", "Oferta")
        )

        // Configurar RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewNotifications)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = NotificationAdapter(notifications)
        recyclerView.adapter = adapter

        // Configurar el campo de búsqueda
        val searchEditText = view.findViewById<EditText>(R.id.searchEditText)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filter(s.toString())
            }
        })

        // Configurar botones de filtro
        view.findViewById<Button>(R.id.filterAllButton).setOnClickListener {
            filterByType(null)
        }
        view.findViewById<Button>(R.id.filterOrdersButton).setOnClickListener {
            filterByType("Pedido")
        }
        view.findViewById<Button>(R.id.filterOffersButton).setOnClickListener {
            filterByType("Oferta")
        }
    }

    // Filtrar por texto de búsqueda
    private fun filter(text: String) {
        val filteredNotifications = notifications.filter {
            it.title.contains(text, ignoreCase = true) || it.subtitle.contains(text, ignoreCase = true)
        }
        adapter.filterList(filteredNotifications)
    }

    // Filtrar por tipo (e.g., "Pedido", "Oferta")
    private fun filterByType(type: String?) {
        val filteredNotifications = if (type == null) {
            notifications
        } else {
            notifications.filter { it.type == type }
        }
        adapter.filterList(filteredNotifications)
    }
}