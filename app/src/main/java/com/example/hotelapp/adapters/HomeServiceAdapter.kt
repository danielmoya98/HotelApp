package com.example.hotelapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotelapp.R
import com.example.hotelapp.dataclass.Servicio
import io.github.jan.supabase.storage.Storage

class HomeServiceAdapter(
    private val serviceList: List<Servicio>,
    private val storage: Storage ,
    private val onItemClick: (Servicio) -> Unit
) : RecyclerView.Adapter<HomeServiceAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgServicio: ImageView = view.findViewById(R.id.imgProducto)
        val tvNombreServicio: TextView = view.findViewById(R.id.tvNombreProducto)
        val tvPrecioServicio: TextView = view.findViewById(R.id.tvPrecioProducto)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
        val tvRatingValue: TextView = view.findViewById(R.id.tvRatingValue)
        val tvDescripcionServicio: TextView = view.findViewById(R.id.tvDescripcionProducto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = serviceList[position]

        holder.tvNombreServicio.text = service.nombre
        holder.tvPrecioServicio.text = "${service.precio} Bs"
        holder.ratingBar.rating = 4.0f // Ajusta según tu lógica
        holder.tvRatingValue.text = "4.0" // Ajusta según tu lógica
        holder.tvDescripcionServicio.text = service.descripcion

        // Cargar imagen con Glide
        Glide.with(holder.imgServicio.context)
            .load(service.imagen_url)
            .placeholder(R.drawable.sauna) // Imagen de placeholder
            .error(R.drawable.sauna) // Imagen en caso de error
            .into(holder.imgServicio)

        holder.itemView.setOnClickListener {
            onItemClick(service) // Llamar al listener al hacer clic
        }

    }

    override fun getItemCount(): Int = serviceList.size
}
