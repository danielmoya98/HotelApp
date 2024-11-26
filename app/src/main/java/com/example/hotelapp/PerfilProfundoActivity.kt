package com.example.hotelapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.hotelapp.utils.UserSession
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import io.ktor.client.utils.EmptyContent.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class PerfilProfundoActivity : AppCompatActivity() {

    private val supabase = createSupabaseClient(
        supabaseUrl = "https://xjxpeyqzvalgoursiqxb.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhqeHBleXF6dmFsZ291cnNpcXhiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzE0MzY0MTIsImV4cCI6MjA0NzAxMjQxMn0.xL5oNgjXi4LWBkmWezvtUYs6PMsPTe4lCviQkYUYPek"
    ) {
        install(Postgrest)
        install(Storage)
    }

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_profundo)

        // Referencias a los elementos del diseño
        val etFullName = findViewById<TextInputEditText>(R.id.etFullName)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val etTelefono = findViewById<TextInputEditText>(R.id.etPassword)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword2)
        val btnGuardarCambios = findViewById<MaterialButton>(R.id.editProfileButton)
        val cambiarFotoButton = findViewById<MaterialButton>(R.id.CambiarFotoButton)
        val profileImageView = findViewById<CircleImageView>(R.id.profile_image)

        // Cargar los datos actuales del usuario desde UserSession
        etFullName.setText(UserSession.userName)
        etEmail.setText(UserSession.userEmail)
        etTelefono.setText(UserSession.userTel)
        etPassword.setText(UserSession.userPass)

        // Cargar foto de perfil actual
        UserSession.userPhotoUrl?.let { photoUrl ->
            Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.perfil) // Imagen por defecto
                .error(R.drawable.perfil) // Imagen de error
                .into(profileImageView)
        }

        // Seleccionar nueva foto
        cambiarFotoButton.setOnClickListener {
            selectPhotoFromGallery()
        }

        // Guardar cambios en el perfil
        btnGuardarCambios.setOnClickListener {
            val nuevoNombre = etFullName.text.toString().trim()
            val nuevoEmail = etEmail.text.toString().trim()
            val nuevoTelefono = etTelefono.text.toString().trim()
            val nuevoPassword = etPassword.text.toString().trim()

            if (nuevoNombre.isEmpty() || nuevoEmail.isEmpty() || nuevoTelefono.isEmpty() || nuevoPassword.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            } else {
                actualizarPerfil(nuevoNombre, nuevoEmail, nuevoTelefono, nuevoPassword)
            }
        }
    }

    private fun actualizarPerfil(nombre: String, email: String, telefono: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = UserSession.userId ?: throw Exception("Usuario no autenticado.")

                // Subir nueva foto si se seleccionó
                var newPhotoUrl: String? = UserSession.userPhotoUrl
                if (selectedImageUri != null) {
                    val bucket = supabase.storage["avatars"]
                    val fileName = "${UUID.randomUUID()}.jpg"

                    // Convertir InputStream a ByteArray
                    val inputStream = contentResolver.openInputStream(selectedImageUri!!)
                        ?: throw Exception("Error al abrir la imagen seleccionada.")
                    val byteArray = inputStream.readBytes()

                    // Subir la imagen al bucket de Supabase Storage
                    bucket.upload(fileName, byteArray)

                    // Generar URL pública de la foto subida
                    newPhotoUrl = "https://xjxpeyqzvalgoursiqxb.supabase.co/storage/v1/object/public/avatars/$fileName"
                }

                // Actualizar datos en Supabase
                supabase.from("usuarios").update(
                    mapOf(
                        "nombre" to nombre,
                        "correo" to email,
                        "telefono" to telefono,
                        "contrasena" to password,
                        "foto_url" to newPhotoUrl
                    )
                ) {
                    filter {
                        eq("id", userId) // Filtra por el ID del usuario
                    }
                }

                withContext(Dispatchers.Main) {
                    // Actualizar UserSession
                    UserSession.userName = nombre
                    UserSession.userEmail = email
                    UserSession.userTel = telefono
                    UserSession.userPass = password
                    UserSession.userPhotoUrl = newPhotoUrl

                    Toast.makeText(this@PerfilProfundoActivity, "Perfil actualizado correctamente.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@PerfilProfundoActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun selectPhotoFromGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        galleryLauncher.launch(intent)
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            selectedImageUri = result.data?.data
            val profileImageView = findViewById<CircleImageView>(R.id.profile_image)
            profileImageView.setImageURI(selectedImageUri)
        } else {
            Toast.makeText(this, "No se seleccionó ninguna imagen.", Toast.LENGTH_SHORT).show()
        }
    }
}
