    package com.example.hotelapp

    import android.content.Intent
    import android.os.Bundle
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.ActionBarDrawerToggle
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.GravityCompat
    import androidx.core.view.forEach
    import androidx.drawerlayout.widget.DrawerLayout
    import androidx.fragment.app.Fragment
    import com.google.android.material.navigation.NavigationView

    class NavegacionActivity : AppCompatActivity() {
        private lateinit var drawerLayout: DrawerLayout
        private lateinit var navigationView: NavigationView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_navegacion)

            drawerLayout = findViewById(R.id.drawer_layout)
            navigationView = findViewById(R.id.navigation_view)

            setSupportActionBar(findViewById(R.id.toolbar))

            // Configuración del Drawer Toggle
            val toggle = ActionBarDrawerToggle(
                this, drawerLayout, findViewById(R.id.toolbar),
                R.string.navigation_drawer_open, R.string.navigation_drawer_close
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            // Cargar el fragmento por defecto (Inicio)
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, HomeFragment())
                    .commit()
                navigationView.setCheckedItem(R.id.nav_home)
            }

            // Manejar la navegación
            navigationView.setNavigationItemSelectedListener { menuItem ->
                // Desmarcar otros elementos
                navigationView.menu.forEach { item ->
                    item.isChecked = false
                }

                val fragment = when (menuItem.itemId) {
                    R.id.nav_home -> {
                        supportActionBar?.title = "Inicio"
                        HomeFragment()
                    }
                    R.id.nav_profile -> {
                        supportActionBar?.title = "Perfil"
                        ProfileFragment()
                    }
                    R.id.nav_notifications -> {
                        supportActionBar?.title = "Notificaciones"
                        NotificationsFragment()
                    }
                    R.id.nav_orders -> {
                        supportActionBar?.title = "Pedidos"
                        OrdersFragment()
                    }
                    R.id.nav_search -> {
                        supportActionBar?.title = "Buscar"
                        SearchFragment()
                    }
                    R.id.nav_location -> {
                        supportActionBar?.title = "Ubicaciones"
                        LocationFragment()
                    }
                    R.id.nav_help -> {
                        supportActionBar?.title = "Ayuda"
                        HelpFragment()
                    }
                    R.id.nav_about -> {
                        supportActionBar?.title = "Acerca de"
                        AboutFragment()
                    }
                    R.id.nav_logout -> {
                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                        null
                    }
                    else -> null
                }

                fragment?.let {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.content_frame, it)
                        .commit()
                }

                // Marca el elemento seleccionado
                menuItem.isChecked = true

                drawerLayout.closeDrawer(GravityCompat.START)
                true
            }
        }

        override fun onBackPressed() {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                super.onBackPressed()
            }
        }
    }