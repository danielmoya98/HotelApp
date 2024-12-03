package com.example.hotelapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.R
import com.example.hotelapp.dataclass.Notificacion

class NotificacionesAdapter(private val notificaciones: List<Notificacion>) :
    RecyclerView.Adapter<NotificacionesAdapter.NotificacionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificacionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificacionViewHolder, position: Int) {
        val notificacion = notificaciones[position]
        holder.mensajeTextView.text = notificacion.mensaje
        holder.fechaEnvioTextView.text = notificacion.fecha_envio ?: "Fecha no disponible"
    }

    override fun getItemCount(): Int = notificaciones.size

    class NotificacionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mensajeTextView: TextView = itemView.findViewById(R.id.mensajeTextView)
        val fechaEnvioTextView: TextView = itemView.findViewById(R.id.fechaEnvioTextView)
    }
}
