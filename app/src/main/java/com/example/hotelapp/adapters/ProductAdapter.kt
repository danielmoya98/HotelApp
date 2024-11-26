package com.example.hotelapp.adapters

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.R

data class Item(
    val name: String,
    val category: String,
    val description: String,
    val price: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(category)
        parcel.writeString(description)
        parcel.writeDouble(price)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item = Item(parcel)
        override fun newArray(size: Int): Array<Item?> = arrayOfNulls(size)
    }
}

class ProductAdapter(
    private val items: List<Item>,
    private val onItemClick: (Item) -> Unit // Agregamos el click listener como parámetro
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener { onItemClick(item) } // Configura el clic para cada elemento
    }

    override fun getItemCount(): Int = items.size

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.tvNombreProducto)
        private val priceTextView: TextView = itemView.findViewById(R.id.tvPrecioProducto)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescripcionProducto)
        private val imageView: ImageView = itemView.findViewById(R.id.imgProducto)

        fun bind(item: Item) {
            nameTextView.text = item.name
            priceTextView.text = "${item.price}bs"
            descriptionTextView.text = item.description

            // Asigna una imagen según el nombre o categoría del producto
            imageView.setImageResource(R.drawable.sauna) // Puedes cambiar esto para asignar imágenes dinámicamente
        }
    }
}
