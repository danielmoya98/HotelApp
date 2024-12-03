package com.example.hotelapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.adapters.ReservaAdapter
import com.example.hotelapp.dataclass.Reserva
import com.example.hotelapp.utils.UserSession
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrdersFragment : Fragment() {

    // Crear cliente Supabase
    private val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://xjxpeyqzvalgoursiqxb.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhqeHBleXF6dmFsZ291cnNpcXhiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzE0MzY0MTIsImV4cCI6MjA0NzAxMjQxMn0.xL5oNgjXi4LWBkmWezvtUYs6PMsPTe4lCviQkYUYPek"
    ){
        install(Postgrest)
    }


    private lateinit var reservaAdapter: ReservaAdapter
    private lateinit var recyclerView: RecyclerView
    private val reservas = mutableListOf<Reserva>() // Lista de reservas

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.ordersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Configurar el adapter
        reservaAdapter = ReservaAdapter(reservas)
        recyclerView.adapter = reservaAdapter

        // Cargar las reservas desde Supabase
        loadReservas()
    }

    // Cargar reservas desde Supabase
    private fun loadReservas() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // Consultamos las reservas desde Supabase
                val response = supabaseClient.from("reservas")
                    .select()
                    {
                        filter {
                            eq("estado", "confirmado") // Filtra por el ID del usuario
                            UserSession.userId?.let { eq("usuario_id", it) }
                        }
                    }
                    .decodeList<Reserva>()


                withContext(Dispatchers.Main) {
                    reservas.clear() // Limpiar la lista antes de agregar los nuevos datos
                    reservas.addAll(response) // Agregar las reservas a la lista
                    reservaAdapter.notifyDataSetChanged() // Notificar al adaptador que los datos han cambiado
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error al cargar las reservas", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
