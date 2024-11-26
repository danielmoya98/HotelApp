package com.example.hotelapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.R

data class CardItem(val title: String, val description: String)

class ExpandableCardAdapter(private val items: List<CardItem>) :
    RecyclerView.Adapter<ExpandableCardAdapter.CardViewHolder>() {

    private val expandedPositions = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_card, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item, expandedPositions.contains(position))

        holder.itemView.setOnClickListener {
            if (expandedPositions.contains(position)) {
                expandedPositions.remove(position)
            } else {
                expandedPositions.add(position)
            }
            notifyItemChanged(position)  // Notify change for animation
        }
    }

    override fun getItemCount(): Int = items.size

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val description: TextView = itemView.findViewById(R.id.description)

        fun bind(item: CardItem, isExpanded: Boolean) {
            title.text = item.title
            description.text = item.description
            description.visibility = if (isExpanded) View.VISIBLE else View.GONE
            description.animate().alpha(if (isExpanded) 1f else 0f).setDuration(300).start()
        }
    }
}
