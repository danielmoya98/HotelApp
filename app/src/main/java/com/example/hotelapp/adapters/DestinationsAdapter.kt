package com.example.hotelapp.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.R

data class Destinations(val imageResId: Int, val name: String, val description: String)

class DestinationsAdapter(private val destinations: List<Destinations>) : RecyclerView.Adapter<DestinationsAdapter.DestinationsViewHolder>() {

    inner class DestinationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgDestination: ImageView = itemView.findViewById(R.id.imgDestination)
        val tvDestinationName: TextView = itemView.findViewById(R.id.tvDestinationName)
        val tvDestinationDescription: TextView = itemView.findViewById(R.id.tvDestinationDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_destinations, parent, false)
        return DestinationsViewHolder(view)
    }

    override fun onBindViewHolder(holder: DestinationsViewHolder, position: Int) {
        val destination = destinations[position]
        holder.imgDestination.setImageResource(destination.imageResId)
        holder.tvDestinationName.text = destination.name
        holder.tvDestinationDescription.text = destination.description
    }

    override fun getItemCount() = destinations.size
}


