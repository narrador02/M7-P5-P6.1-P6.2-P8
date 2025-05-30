package com.example.m07_p5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.m07_p5.adapter.AlimentoAdapter
import com.example.m07_p5.viewmodel.AlimentoViewModel
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity() {

    private val viewModel: AlimentoViewModel by viewModels()
    private lateinit var adapter: AlimentoAdapter
    private lateinit var txtCalories: TextView
    private lateinit var txtMacros: TextView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configuración del menú lateral
        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.navigation_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, findViewById(R.id.toolbar),
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

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

        adapter = AlimentoAdapter(viewModel.alimentos) { position ->
            viewModel.deleteAlimento(position, {
                adapter.notifyItemRemoved(position)
                Toast.makeText(this, "Alimento eliminado", Toast.LENGTH_SHORT).show()
            }, { error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            })
        }
        // Configuración del Dashboard
        txtCalories = findViewById(R.id.txt_calories)
        txtMacros = findViewById(R.id.txt_macros)

        actualizarDashboard()

        // Fetch inicial de alimentos
        viewModel.fetchAlimentos({
            adapter.notifyDataSetChanged()
        }, { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        })

        // Configuración del Bottom Navigation
        setupBottomNavigation(R.id.bottom_navigation, R.id.nav_home)
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

        txtCalories.text = "Calorías consumidas hoy: $caloriasConsumidas kcal"
        txtMacros.text = "Carbohidratos: ${carbohidratos}g | Proteínas: ${proteinas}g | Grasas: ${grasas}g"
    }

    override fun onResume() {
        super.onResume()
        setupBottomNavigation(R.id.bottom_navigation, R.id.nav_home)
    }
}