package com.example.hotelapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.adapters.EmpleadoNotificacionAdapter
import com.example.hotelapp.dataclass.Notificacion
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.CoroutineScope

class EmpleadoNotificationsFragment : Fragment() {

    private lateinit var notificacionesAdapter: EmpleadoNotificacionAdapter
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
        // Infla el layout para este fragmento
        return inflater.inflate(R.layout.fragment_empleado_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configuración del RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewEmpleadoNotifications)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        notificacionesAdapter = EmpleadoNotificacionAdapter(notificaciones) { notificacion ->
            marcarComoLeida(notificacion) // Acción para marcar como leída
        }
        recyclerView.adapter = notificacionesAdapter

        // Cargar las notificaciones
        cargarNotificaciones()
    }

    private fun cargarNotificaciones() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Consultamos las notificaciones desde Supabase
                val response = supabaseClient.from("notificaciones")
                    .select()
                    {
                        filter {
                            eq("leido", false) // Filtra por el ID del usuario
                        }
                    }
                    .decodeList<Notificacion>() // Decodificamos las notificaciones

                // Actualizamos la lista en el hilo principal
                withContext(Dispatchers.Main) {
                    notificaciones.clear()  // Limpiamos la lista de notificaciones
                    notificaciones.addAll(response)  // Agregamos las nuevas notificaciones
                    notificacionesAdapter.notifyDataSetChanged() // Notificamos al adaptador que los datos han cambiado
                }
            } catch (e: Exception) {
                e.printStackTrace()  // Manejo de errores
            }
        }
    }

    private fun marcarComoLeida(notificacion: Notificacion) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Primero, marcamos la notificación como leída en la base de datos
                val updateNotificationResponse = supabaseClient.from("notificaciones")
                    .update(mapOf("leido" to true)) { // Actualiza solo el campo 'leido'
                        filter {
                            // Filtra por reserva_id o pedido_id, dependiendo de cuál esté presente
                            when {
                                notificacion.reserva_id != null -> eq("reserva_id", notificacion.reserva_id)
                                notificacion.pedido_id != null -> eq("pedido_id", notificacion.pedido_id)
                                else -> throw IllegalArgumentException("La notificación no contiene ni reserva_id ni pedido_id.")
                            }
                        }
                    }

                // Verificar si la notificación es sobre una reserva
                if (notificacion.reserva_id != null) {
                    // Actualizar el estado de la reserva
                    val updateReservationResponse = supabaseClient.from("reservas")
                        .update(mapOf("estado" to "confirmado")) {
                            filter {
                                eq("id", notificacion.reserva_id)
                            }
                        }
                }
                // Verificar si la notificación es sobre un pedido
                else if (notificacion.pedido_id != null) {
                    // Actualizar el estado del pedido
                    val updateOrderResponse = supabaseClient.from("pedidos")
                        .update(mapOf("estado" to "procesado")) {
                            filter {
                                eq("id", notificacion.pedido_id)
                            }
                        }
                } else {
                    throw IllegalArgumentException("La notificación no contiene ni reserva_id ni pedido_id.")
                }

                // Si todo es exitoso, actualizamos la lista de notificaciones en el hilo principal
                withContext(Dispatchers.Main) {
                    notificaciones.remove(notificacion)
                    notificaciones.add(notificacion.copy(leido = true))
                    notificacionesAdapter.notifyDataSetChanged()
                }
            } catch (e: Exception) {
                e.printStackTrace() // Manejo de errores
            }
        }
    }



}
