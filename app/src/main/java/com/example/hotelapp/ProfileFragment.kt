package com.example.hotelapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.hotelapp.utils.UserSession
import com.google.android.material.button.MaterialButton
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    private var profileImageView: CircleImageView? = null
    private var nameLabel: TextView? = null
    private var usernameLabel: TextView? = null
    private var editProfileButton: MaterialButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Referencias a los elementos de la vista
        profileImageView = view.findViewById(R.id.profile_image)
        nameLabel = view.findViewById(R.id.nameLabel)
        usernameLabel = view.findViewById(R.id.usernameLabel)
        editProfileButton = view.findViewById(R.id.editProfileButton)

        // Configurar botón para editar perfil
        editProfileButton?.setOnClickListener {
            // Inicia la actividad de edición de perfil
            val intent = Intent(activity, PerfilProfundoActivity::class.java)
            startActivity(intent)
        }

        // Cargar datos iniciales del usuario
        cargarDatosUsuario()

        return view
    }

    override fun onResume() {
        super.onResume()
        // Refrescar datos del perfil desde UserSession
        cargarDatosUsuario()
    }

    private fun cargarDatosUsuario() {
        val userName = UserSession.userName
        val userEmail = UserSession.userEmail
        val userPhotoUrl = UserSession.userPhotoUrl

        if (userName != null && userEmail != null) {
            nameLabel?.text = userName
            usernameLabel?.text = "@${userEmail.split("@")[0]}" // Crea un nombre de usuario simple basado en el email
        } else {
            Toast.makeText(activity, "Error al cargar los datos del usuario", Toast.LENGTH_SHORT).show()
        }

        // Cargar imagen de perfil con Glide
        profileImageView?.let {
            Glide.with(this)
                .load(userPhotoUrl)
                .placeholder(R.drawable.perfil) // Imagen por defecto
                .error(R.drawable.perfil) // Imagen de error
                .into(it)
        }
    }
}
