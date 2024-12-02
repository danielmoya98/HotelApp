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
import com.example.hotelapp.adapters.Order
import com.example.hotelapp.adapters.OrderAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText

class OrdersFragment : Fragment() {

    private lateinit var orderAdapter: OrderAdapter
    private val orders = listOf(
        Order("iPhone 13 Pro", "En preparación", 999.99),
        Order("Samsung Galaxy S21", "En tránsito", 799.99),
        // Agrega más pedidos aquí
    )
    private var filteredOrders = orders

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicializamos RecyclerView
        val ordersRecyclerView = view.findViewById<RecyclerView>(R.id.ordersRecyclerView)
        orderAdapter = OrderAdapter(filteredOrders)
        ordersRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        ordersRecyclerView.adapter = orderAdapter




    }

    private fun filterOrders(query: String) {
        val selectedTab = view?.findViewById<TabLayout>(R.id.tabLayout)?.selectedTabPosition ?: 0

        filteredOrders = orders.filter {
            val matchesQuery = it.name.lowercase().contains(query.lowercase())
            val matchesStatus = when (selectedTab) {
                0 -> it.status == "En preparación" || it.status == "En tránsito"
                1 -> it.status == "Finalizado"
                else -> true
            }
            matchesQuery && matchesStatus
        }
        orderAdapter.updateOrders(filteredOrders)
    }
}