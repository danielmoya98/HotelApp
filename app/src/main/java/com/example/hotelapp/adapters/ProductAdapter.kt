package com.example.hotelapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotelapp.R
import com.example.hotelapp.dataclass.Producto
import io.github.jan.supabase.storage.Storage

class ProductAdapter(
    private val productList: List<Producto>,
    private val storage: Storage // Instancia de Supabase Storage
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgProducto: ImageView = view.findViewById(R.id.imgProducto)
        val tvNombreProducto: TextView = view.findViewById(R.id.tvNombreProducto)
        val tvPrecioProducto: TextView = view.findViewById(R.id.tvPrecioProducto)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
        val tvRatingValue: TextView = view.findViewById(R.id.tvRatingValue)
        val tvDescripcionProducto: TextView = view.findViewById(R.id.tvDescripcionProducto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = productList[position]

        holder.tvNombreProducto.text = product.nombre
        holder.tvPrecioProducto.text = "${product.precio} Bs"
        holder.ratingBar.rating = 4.0f // Ajusta según tu lógica
        holder.tvRatingValue.text = "4.0" // Ajusta según tu lógica
        holder.tvDescripcionProducto.text = product.descripcion


        val publicUrl = storage.from("productos").publicUrl(product.imagen_url)

        Log.d("ProductAdapter", "Generated public URL: $publicUrl")

        // Cargar imagen con Glide
        Glide.with(holder.imgProducto.context)
            .load(product.imagen_url)
            .placeholder(R.drawable.sauna) // Imagen de placeholder
            .error(R.drawable.sauna) // Imagen en caso de error
            .into(holder.imgProducto)
    }

    override fun getItemCount(): Int = productList.size
}
