package com.example.hotelapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapp.adapters.CardItem
import com.example.hotelapp.adapters.ExpandableCardAdapter
import com.google.android.material.card.MaterialCardView

class HelpFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_help, container, false)

        // Set up the back button
        val backButton = view.findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        // Contact support button
        val contactSupportButton = view.findViewById<Button>(R.id.contactSupportButton)
        contactSupportButton.setOnClickListener {
            Toast.makeText(context, "Contacting support...", Toast.LENGTH_SHORT).show()
        }

        // Get user comment
        val userComment = view.findViewById<EditText>(R.id.userComment)
        userComment.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val commentText = userComment.text.toString()
                if (commentText.isNotEmpty()) {
                    Toast.makeText(context, "Comment saved.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Set up the RecyclerView with expandable cards
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Sample data for the expandable cards
        val items = listOf(
            CardItem("¿Cambiar contraseña?", "Description for Card 1"),
            CardItem("¿Olvidates tu contraseña?", "Description for Card 2")
        )

        // Initialize and set the adapter
        val adapter = ExpandableCardAdapter(items)
        recyclerView.adapter = adapter

        return view
    }
}
