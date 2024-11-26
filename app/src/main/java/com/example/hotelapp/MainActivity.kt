package com.example.hotelapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hotelapp.dataclass.Usuario
import com.example.hotelapp.utils.UserSession // Importar UserSession
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var editTextCorreo: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button

    private val supabase: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://xjxpeyqzvalgoursiqxb.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhqeHBleXF6dmFsZ291cnNpcXhiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzE0MzY0MTIsImV4cCI6MjA0NzAxMjQxMn0.xL5oNgjXi4LWBkmWezvtUYs6PMsPTe4lCviQkYUYPek"
    ) {
        install(Postgrest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextCorreo = findViewById(R.id.textFieldUsername)
        editTextPassword = findViewById(R.id.textFieldPassword)
        buttonLogin = findViewById(R.id.signInButton)

        buttonLogin.setOnClickListener {
            val correoIngresado = editTextCorreo.text.toString()
            val passwordIngresado = editTextPassword.text.toString()
            verificarUsuario(correoIngresado, passwordIngresado)
        }
    }

    private fun verificarUsuario(correo: String, password: String) {
        lifecycleScope.launch {
            val usuarios = withContext(Dispatchers.IO) {
                supabase.from("usuarios")
                    .select()
                    .decodeList<Usuario>()
            }

            val usuarioValido = usuarios.find {
                it.correo == correo && it.contrasena == password
            }

            if (usuarioValido != null) {
                iniciarSesion(usuarioValido)
            } else {
                mostrarError("Correo o contraseña incorrectos.")
            }
        }
    }

    private fun iniciarSesion(usuario: Usuario) {
        // Guardar datos en UserSession
        UserSession.userId = usuario.id
        UserSession.userName = usuario.nombre
        UserSession.userEmail = usuario.correo
        UserSession.userTel = usuario.telefono
        UserSession.userPass = usuario.contrasena
        UserSession.userRole = usuario.rol
        UserSession.userPhotoUrl = usuario.fotoUrl

        // Navegar a NavegacionActivity
        val intent = Intent(this, NavegacionActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun mostrarError(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
