package com.example.hotelapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.R
import com.example.hotelapp.dataclass.Notificacion

class EmpleadoNotificacionAdapter (
    private val notificaciones: List<Notificacion>,
    private val onConfirmar: (Notificacion) -> Unit
) : RecyclerView.Adapter<EmpleadoNotificacionAdapter.NotificacionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificacionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notificacion_empleado, parent, false)
        return NotificacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificacionViewHolder, position: Int) {
        val notificacion = notificaciones[position]
        holder.mensajeTextView.text = notificacion.mensaje
        holder.fechaEnvioTextView.text = notificacion.fecha_envio ?: "Fecha no disponible"

        // Si la notificación no ha sido leída, mostrar el botón para confirmarla
        if (!notificacion.leido) {
            holder.confirmarButton.visibility = View.VISIBLE
            holder.confirmarButton.setOnClickListener {
                onConfirmar(notificacion)
                holder.confirmarButton.visibility = View.GONE  // Ocultar el botón después de confirmarlo
            }
        } else {
            holder.confirmarButton.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = notificaciones.size

    class NotificacionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mensajeTextView: TextView = itemView.findViewById(R.id.mensajeTextView)
        val fechaEnvioTextView: TextView = itemView.findViewById(R.id.fechaEnvioTextView)
        val confirmarButton: Button = itemView.findViewById(R.id.confirmarButton)
    }
}