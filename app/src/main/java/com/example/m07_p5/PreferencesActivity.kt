package com.example.m07_p5

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class PreferencesActivity : BaseActivity() {

    private lateinit var switchDarkMode: Switch
    private lateinit var btnSavePreferences: Button
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        // Cargar SharedPreferences
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)

        // Aplicar el modo oscuro antes de cargar el layout
        aplicarTemaDesdePreferencias()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        setupBottomNavigation(R.id.bottom_navigation, R.id.nav_settings)

        // Configurar Toolbar y DrawerLayout (Menú lateral)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.navigation_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, findViewById(R.id.toolbar),
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Configurar navegación lateral
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> navegarSiNoEstaEn(MainActivity::class.java)
                R.id.nav_list -> navegarSiNoEstaEn(ListActivity::class.java)
                R.id.nav_settings -> {} // Ya estamos en esta actividad
            }
            drawerLayout.closeDrawers()
            true
        }

        // Configurar el Switch del modo oscuro
        switchDarkMode = findViewById(R.id.switch_dark_mode)
        switchDarkMode.isChecked = sharedPreferences.getBoolean("dark_mode", false)

        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            cambiarModoOscuro(isChecked)
        }

        // Configurar el botón de guardar preferencias
        btnSavePreferences = findViewById(R.id.btn_save_preferences)
        btnSavePreferences.setOnClickListener {
            Toast.makeText(this, "Preferencias guardadas", Toast.LENGTH_SHORT).show()
        }
    }

    private fun aplicarTemaDesdePreferencias() {
        val modoOscuro = sharedPreferences.getBoolean("dark_mode", false)
        val modoActual = AppCompatDelegate.getDefaultNightMode()

        val nuevoModo = if (modoOscuro) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        if (modoActual != nuevoModo) {
            AppCompatDelegate.setDefaultNightMode(nuevoModo)
        }
    }

    private fun cambiarModoOscuro(activarModoOscuro: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("dark_mode", activarModoOscuro)
        editor.apply()

        val nuevoModo = if (activarModoOscuro) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }

        if (AppCompatDelegate.getDefaultNightMode() != nuevoModo) {
            AppCompatDelegate.setDefaultNightMode(nuevoModo)
            finish() // Cierra la actividad actual para que se recargue completamente
            startActivity(intent) // La reabre sin parpadeos
        }
    }

    private fun navegarSiNoEstaEn(destino: Class<*>) {
        if (this::class.java != destino) {
            Log.d("BottomNav", "Navegando a ${destino.simpleName}")
            val intent = Intent(this, destino)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
