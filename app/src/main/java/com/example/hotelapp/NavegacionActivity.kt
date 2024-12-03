package com.example.hotelapp

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.forEach
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.hotelapp.dataclass.Usuario
import com.example.hotelapp.utils.UserSession
import com.google.android.material.navigation.NavigationView
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NavegacionActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView

    private val supabase: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://xjxpeyqzvalgoursiqxb.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhqeHBleXF6dmFsZ291cnNpcXhiIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzE0MzY0MTIsImV4cCI6MjA0NzAxMjQxMn0.xL5oNgjXi4LWBkmWezvtUYs6PMsPTe4lCviQkYUYPek"
    ) {
        install(Postgrest)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navegacion)

        // Inicializar vistas
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.navigation_view)

        // Verificar sesión del usuario
        if (UserSession.userId == null) {
            Toast.makeText(this, "Sesión expirada. Por favor, inicia sesión.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            return
        }

        val userRole = UserSession.userRole
        when (userRole) {
            "empleado" -> {
                navigationView.inflateMenu(R.menu.menu_empleado)
                configurarNavegacionParaEmpleado()
            }
            "cliente" -> {
                navigationView.inflateMenu(R.menu.drawer_menu)
                configurarNavegacion() // Método existente para cliente
            }
            else -> {
                navigationView.inflateMenu(R.menu.drawer_menu) // Menú por defecto
                configurarNavegacion()
            }
        }

        // Configurar toolbar y toggle del drawer
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.arrow)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Cargar el fragmento inicial si es la primera vez
        if (savedInstanceState == null) {
            val defaultFragment = if (userRole == "empleado") RegistrarProductoFragment() else HomeFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.content_frame, defaultFragment)
                .commit()

            // Marcar el elemento inicial en el menú
            navigationView.setCheckedItem(if (userRole == "empleado") R.id.nav_productos else R.id.nav_home)
        }

        // Cargar datos del usuario
        cargarDatosUsuario()
    }

    override fun onResume() {
        super.onResume()
        // Refrescar datos del usuario
        actualizarDrawerHeader()
    }

    private fun cargarDatosUsuario() {
        lifecycleScope.launch {
            try {
                val userId = UserSession.userId ?: throw Exception("Usuario no autenticado.")
                val userDetails = withContext(Dispatchers.IO) {
                    supabase.from("usuarios").select(columns = Columns.list("id", "nombre", "contrasena", "correo", "telefono", "rol", "foto_url")) {
                        filter { eq("id", userId) }
                    }.decodeSingle<Usuario>()
                }

                // Guardar datos del usuario en la sesión
                UserSession.userName = userDetails.nombre
                UserSession.userEmail = userDetails.correo
                UserSession.userTel = userDetails.telefono
                UserSession.userPass = userDetails.contrasena
                UserSession.userRole = userDetails.rol
                UserSession.userPhotoUrl = userDetails.fotoUrl

                // Actualizar el encabezado del drawer
                actualizarDrawerHeader()
            } catch (e: Exception) {
                Toast.makeText(this@NavegacionActivity, "Error al cargar datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun actualizarDrawerHeader() {
        val headerView = navigationView.getHeaderView(0)
        val userNameText = headerView.findViewById<TextView>(R.id.user_name)
        val userEmailText = headerView.findViewById<TextView>(R.id.user_email)
        val userBioText = headerView.findViewById<TextView>(R.id.user_bio)
        val profileImageView = headerView.findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.profile_image)

        userNameText.text = UserSession.userName ?: "Usuario"
        userEmailText.text = UserSession.userEmail ?: "Sin correo"
        userBioText.text = "Rol: ${UserSession.userRole ?: "N/A"}"

        UserSession.userPhotoUrl?.let { photoUrl ->
            Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.perfil)
                .error(R.drawable.perfil)
                .into(profileImageView)
        }
    }

    private fun configurarNavegacionParaEmpleado() {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            navigationView.menu.forEach { it.isChecked = false }
            menuItem.isChecked = true
            val fragment = when (menuItem.itemId) {
                R.id.nav_profile ->  ProfileFragment()
                R.id.nav_notis -> EmpleadoNotificationsFragment()
                R.id.nav_productos -> RegistrarProductoFragment()
                R.id.nav_servicios -> RegistrarServicioFragment()
                R.id.nav_help -> HelpFragment()

                R.id.nav_logout -> {
                    cerrarSesion()
                    null
                }
                else -> null
            }
            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, it)
                    .commit()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun configurarNavegacion() {
        navigationView.setNavigationItemSelectedListener { menuItem ->
            navigationView.menu.forEach { it.isChecked = false }
            menuItem.isChecked = true
            val fragment = when (menuItem.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_profile -> ProfileFragment()
                R.id.nav_notifications -> NotificationsFragment()
                R.id.nav_orders -> OrdersFragment()
                R.id.nav_search -> SearchFragment()
                R.id.nav_location -> LocationFragment()
                R.id.nav_help -> HelpFragment()
                R.id.nav_about -> AboutFragment()

                R.id.nav_logout -> {
                    cerrarSesion()
                    null
                }
                else -> null
            }
            fragment?.let {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, it)
                    .commit()
            }
            drawerLayout.closeDrawer(GravityCompat.START)
            true
        }
    }

    private fun cerrarSesion() {
        UserSession.clearSession()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
