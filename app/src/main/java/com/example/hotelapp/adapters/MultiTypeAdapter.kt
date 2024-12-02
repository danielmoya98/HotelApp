package com.example.hotelapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotelapp.R
import com.example.hotelapp.dataclass.Servicio
import io.github.jan.supabase.storage.Storage
import com.example.hotelapp.dataclass.Producto

class MultiTypeAdapter(
    private val itemList: List<Any>,
    private val storage: Storage
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_PRODUCT = 0
        const val TYPE_SERVICE = 1
        const val TAG = "MultiTypeAdapter" // Etiqueta para los logs
    }

    override fun getItemViewType(position: Int): Int {
        return when (itemList[position]) {
            is Producto -> TYPE_PRODUCT
            is Servicio -> TYPE_SERVICE
            else -> throw IllegalArgumentException("Unknown item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_PRODUCT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_search, parent, false)
                ProductAdapter.ProductViewHolder(view)
            }

            TYPE_SERVICE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_search, parent, false)
                ServiceAdapter.ServiceViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = itemList[position]) {
            is Producto -> {
                val productHolder = holder as ProductAdapter.ProductViewHolder
                productHolder.tvNombreProducto.text = item.nombre
                productHolder.tvPrecioProducto.text = "${item.precio} Bs"
                productHolder.ratingBar.rating = 4.0f
                productHolder.tvRatingValue.text = "4.0"
                productHolder.tvDescripcionProducto.text = item.descripcion

                // Log para depurar la URL del producto
                Log.d(TAG, "Producto imagen_url: ${item.imagen_url}")

                Glide.with(productHolder.imgProducto.context)
                    .load(item.imagen_url)
                    .placeholder(R.drawable.sauna)
                    .error(R.drawable.sauna)
                    .into(productHolder.imgProducto)
            }

            is Servicio -> {
                val serviceHolder = holder as ServiceAdapter.ServiceViewHolder
                serviceHolder.tvNombreServicio.text = item.nombre
                serviceHolder.tvPrecioServicio.text = "${item.precio} Bs"
                serviceHolder.ratingBar.rating = 4.0f
                serviceHolder.tvRatingValue.text = "4.0"
                serviceHolder.tvDescripcionServicio.text = item.descripcion

                // Log para depurar la URL del servicio
                Log.d(TAG, "Servicio imagen_url: ${item.imagen_url}")

                Glide.with(serviceHolder.imgServicio.context)
                    .load(item.imagen_url)
                    .placeholder(R.drawable.sauna)
                    .error(R.drawable.sauna)
                    .into(serviceHolder.imgServicio)
            }
        }
    }

    override fun getItemCount(): Int = itemList.size
}
