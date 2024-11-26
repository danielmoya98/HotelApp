package com.example.hotelapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.R

data class Destination(val name: String, val imageResId: Int)

class DestinationAdapter(private val destinations: List<Destination>) :
    RecyclerView.Adapter<DestinationAdapter.DestinationViewHolder>() {

    inner class DestinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBackground: ImageView = itemView.findViewById(R.id.imgBackground)
        val tvDestinationName: TextView = itemView.findViewById(R.id.tvDestinationName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_destination, parent, false)
        return DestinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinationViewHolder, position: Int) {
        val destination = destinations[position]
        holder.imgBackground.setImageResource(destination.imageResId)
        holder.tvDestinationName.text = destination.name
    }

    override fun getItemCount() = destinations.size
}
