package com.example.m07_p5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity() {

    private lateinit var txtCalories: TextView
    private lateinit var txtMacros: TextView
    private lateinit var listFoodHistory: ListView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupBottomNavigation(R.id.bottom_navigation, R.id.nav_home)

        txtCalories = findViewById(R.id.txt_calories)
        txtMacros = findViewById(R.id.txt_macros)
        listFoodHistory = findViewById(R.id.list_food_history)
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.navigation_view)

        // Habilitar el botón del menú lateral
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, findViewById(R.id.toolbar),
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        // Manejo del menú lateral
        navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> navegarSiNoEstaEn(MainActivity::class.java)
                R.id.nav_list -> navegarSiNoEstaEn(ListActivity::class.java)
                R.id.nav_profile -> {
                    Log.d("Navigation", "Clic en Perfil, abriendo LoginActivity")
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                R.id.nav_tracking -> navegarSiNoEstaEn(TrackingActivity::class.java)
                R.id.nav_dates -> navegarSiNoEstaEn(ConsumptionDatesActivity::class.java)
                R.id.nav_add_food -> navegarSiNoEstaEn(AddFoodActivity::class.java)
                R.id.nav_settings -> navegarSiNoEstaEn(PreferencesActivity::class.java)
                R.id.nav_logout -> finishAffinity()
            }
            drawerLayout.closeDrawers()
            true
        }

        actualizarDashboard()
    }

    private fun navegarSiNoEstaEn(destino: Class<*>) {
        if (this::class.java != destino) {
            Log.d("Navigation", "Navegando a ${destino.simpleName}")
            val intent = Intent(this, destino)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    private fun actualizarDashboard() {
        val caloriasConsumidas = 1800
        val carbohidratos = 220
        val proteinas = 90
        val grasas = 60
        val historialAlimentos = listOf("Manzana - 95 kcal", "Pollo - 250 kcal", "Arroz - 200 kcal")

        txtCalories.text = "Calorías consumidas hoy: $caloriasConsumidas kcal"
        txtMacros.text = "Carbohidratos: ${carbohidratos}g | Proteínas: ${proteinas}g | Grasas: ${grasas}g"

        val adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_list_item_1, historialAlimentos)
        listFoodHistory.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        setupBottomNavigation(R.id.bottom_navigation, R.id.nav_home)
    }
}
