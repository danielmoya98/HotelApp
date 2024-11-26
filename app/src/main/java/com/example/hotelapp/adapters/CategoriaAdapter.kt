package com.example.hotelapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.R
import com.example.hotelapp.dataclass.Categoria


class CategoriaAdapter(private var categoriaList: List<Categoria>) :
    RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_categoria, parent, false)
        return CategoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaViewHolder, position: Int) {
        val categoria = categoriaList[position]
        holder.textDescripcion.text = categoria.descripcion
        holder.textCategoria.text = categoria.nombre_categoria
    }

    override fun getItemCount(): Int = categoriaList.size

    fun updateCategorias(newCategorias: List<Categoria>) {
        categoriaList = newCategorias
        notifyDataSetChanged()
    }

    inner class CategoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDescripcion:TextView = itemView.findViewById(R.id.textDescripcion)
        val textCategoria: TextView = itemView.findViewById(R.id.textCategoria)
    }
}
