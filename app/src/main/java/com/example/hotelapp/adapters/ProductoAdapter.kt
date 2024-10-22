package com.example.hotelapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.R

data class Producto(
    val nombre: String,
    val descripcion: String,
    val imagen: Int
)


class ProductoAdapter(private val productoList: List<Producto>) :
    RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = productoList[position]
        holder.imgProducto.setImageResource(producto.imagen)
        holder.tvNombreProducto.text = producto.nombre
        holder.tvDescripcionProducto.text = producto.descripcion
    }

    override fun getItemCount(): Int = productoList.size

    inner class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducto: ImageView = itemView.findViewById(R.id.imgProducto)
        val tvNombreProducto: TextView = itemView.findViewById(R.id.tvNombreProducto)
        val tvDescripcionProducto: TextView = itemView.findViewById(R.id.tvDescripcionProducto)
    }
}
