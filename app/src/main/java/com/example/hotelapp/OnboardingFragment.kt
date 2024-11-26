package com.example.hotelapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class OnboardingFragment : Fragment() {

    companion object {
        private const val ARG_LAYOUT_ID = "layout_id"
        private const val ARG_TITLE = "title"
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_IMAGE = "image"

        // Método para crear instancias de OnboardingFragment con argumentos personalizados
        fun newInstance(layoutId: Int, title: String, description: String, imageResId: Int): OnboardingFragment {
            val fragment = OnboardingFragment()
            val args = Bundle()
            args.putInt(ARG_LAYOUT_ID, layoutId)
            args.putString(ARG_TITLE, title)
            args.putString(ARG_DESCRIPTION, description)
            args.putInt(ARG_IMAGE, imageResId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Recupera el layout desde los argumentos
        val layoutId = arguments?.getInt(ARG_LAYOUT_ID) ?: 0
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recupera los argumentos
        val title = arguments?.getString(ARG_TITLE)
        val description = arguments?.getString(ARG_DESCRIPTION)
        val imageResId = arguments?.getInt(ARG_IMAGE)

        // Configura los elementos de la vista si existen en el layout
        view.findViewById<TextView>(R.id.titleTextView)?.text = title
        view.findViewById<TextView>(R.id.descriptionTextView)?.text = description
        view.findViewById<ImageView>(R.id.imageView)?.setImageResource(imageResId ?: 0)
    }
}