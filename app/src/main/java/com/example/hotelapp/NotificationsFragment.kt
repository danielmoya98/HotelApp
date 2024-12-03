package com.example.hotelapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.adapters.NotificacionesAdapter
import com.example.hotelapp.dataclass.Notificacion
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationsFragment : Fragment() {


    private lateinit var notificacionesAdapter: NotificacionesAdapter
    private val notificaciones = mutableListOf<Notificacion>()

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewNotifications)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        notificacionesAdapter = NotificacionesAdapter(notificaciones)
        recyclerView.adapter = notificacionesAdapter

        // Cargar las notificaciones
        cargarNotificaciones()


    }

    private fun cargarNotificaciones() {
        Log.d("DEBUG", "cargarNotificaciones llamado") // Log para confirmar que la función inicia
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = supabaseClient.from("notificaciones")
                    .select()
                    .decodeList<Notificacion>() // Decodificamos las notificaciones

                Log.d("DEBUG", "Respuesta de la base de datos: $response") // Log para ver la respuesta

                withContext(Dispatchers.Main) {
                    notificaciones.clear()
                    notificaciones.addAll(response)
                    Log.d("DEBUG", "Notificaciones añadidas al adaptador: $notificaciones") // Log para confirmar los datos cargados
                    notificacionesAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                Log.e("DEBUG", "Error al cargar notificaciones: ${e.message}", e)
            }
        }
    }





}