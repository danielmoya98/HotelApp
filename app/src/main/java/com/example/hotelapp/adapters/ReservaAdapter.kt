package com.example.hotelapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.R
import com.example.hotelapp.dataclass.Reserva


class ReservaAdapter(private val reservas: List<Reserva>) : RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_reserva, parent, false)
        return ReservaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val reserva = reservas[position]
        // Asignar los valores del objeto Reserva a los elementos del XML
        holder.productName.text = "Servicio Reservado : ${reserva.servicio_id}" // Ejemplo, podrías obtener el nombre del servicio en lugar del ID
        holder.orderStatus.text = "Estado: ${reserva.estado}"
        holder.productPrice.text = "Usuario que lo reservo: ${reserva.usuario_id}"

    }

    override fun getItemCount(): Int {
        return reservas.size
    }

    // ViewHolder que contiene las referencias a los elementos del layout
    inner class ReservaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        val productName: TextView = itemView.findViewById(R.id.productName)
        val orderStatus: TextView = itemView.findViewById(R.id.orderStatus)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
    }
}
