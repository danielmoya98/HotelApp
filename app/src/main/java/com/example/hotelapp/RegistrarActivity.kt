package com.example.hotelapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hotelapp.dataclass.Usuario
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.UUID

class RegistrarActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null

    // Inicializar Supabase
    private val supabase = createSupabaseClient(
        supabaseUrl = "https://xjxpeyqzvalgoursiqxb.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhqeHBleXF6dmFsZ291cnNpcXhiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzE0MzY0MTIsImV4cCI6MjA0NzAxMjQxMn0.xL5oNgjXi4LWBkmWezvtUYs6PMsPTe4lCviQkYUYPek"
    ) {
        install(Postgrest)
        install(Storage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar)

        val etFullName: TextInputEditText = findViewById(R.id.etFullName)
        val etEmail: TextInputEditText = findViewById(R.id.etEmail)
        val etTelefono: TextInputEditText = findViewById(R.id.etPassword)
        val etPassword: TextInputEditText = findViewById(R.id.etPassword2)
        val btnAddPhoto: MaterialButton = findViewById(R.id.btnAddPhoto)
        val btnCreateAccount: MaterialButton = findViewById(R.id.btnCreateAccount)
        val profileImageView: CircleImageView = findViewById(R.id.profile_image)

        // Abrir selector de imágenes al pulsar "Subir Foto"
        btnAddPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
            }
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        btnCreateAccount.setOnClickListener {
            val nombre = etFullName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (nombre.isEmpty() || email.isEmpty() || telefono.isEmpty() || password.isEmpty() || imageUri == null) {
                Toast.makeText(this, "Por favor, completa todos los campos y sube una foto", Toast.LENGTH_SHORT).show()
            } else {
                subirFotoYRegistrarUsuario(nombre, email, telefono, password)
            }
        }
    }

    private fun subirFotoYRegistrarUsuario(nombre: String, email: String, telefono: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val bucket = supabase.storage.from("avatars") // Acceso al bucket `avatars`
                val fileName = "${UUID.randomUUID()}.jpg"

                // Convertir InputStream a ByteArray
                val inputStream = contentResolver.openInputStream(imageUri!!)
                val byteArray = inputStream?.readBytes() ?: throw Exception("Error al leer la imagen")

                // Subir imagen a Supabase Storage
                val uploadResponse = bucket.upload(fileName, byteArray)

                val photoUrl = "https://xjxpeyqzvalgoursiqxb.supabase.co/storage/v1/object/public/avatars/$fileName"

                // Insertar usuario en la base de datos
                val response = supabase.postgrest["usuarios"].insert(
                    mapOf(
                        "nombre" to nombre,
                        "correo" to email,
                        "telefono" to telefono,
                        "contrasena" to password,
                        "rol" to "empleado",
                        "foto_url" to photoUrl
                    )
                )
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@RegistrarActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            val profileImageView: CircleImageView = findViewById(R.id.profile_image)
            profileImageView.setImageURI(imageUri)
        }
    }
}
