package com.example.hotelapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.R

data class Categoria(
    val nombre: String,
    val icono: Int
)


class CategoriaAdapter(private val categoriaList: List<Categoria>) :
    RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val categoria = categoriaList[position]
        holder.iconCategoria.setImageResource(categoria.icono)
        holder.textCategoria.text = categoria.nombre
    }

    override fun getItemCount(): Int = categoriaList.size

    inner class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconCategoria: ImageView = itemView.findViewById(R.id.iconCategoria)
        val textCategoria: TextView = itemView.findViewById(R.id.textCategoria)
    }
}
