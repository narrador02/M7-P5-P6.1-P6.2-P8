package com.example.m07_p5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.m07_p5.adapter.AlimentoAdapter
import com.example.m07_p5.data.model.Alimento
import com.example.m07_p5.viewmodel.AlimentoViewModel
import com.google.android.material.navigation.NavigationView

class MainActivity : BaseActivity() {

    private val viewModel: AlimentoViewModel by viewModels()
    private lateinit var adapter: AlimentoAdapter
    private lateinit var txtCalories: TextView
    private lateinit var txtMacros: TextView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var editFoodLauncher: ActivityResultLauncher<Intent>

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
                R.id.nav_statistics -> {
                    val intent = Intent(this, StatisticsActivity::class.java)
                    startActivity(intent)
                }
            }
            drawerLayout.closeDrawers()
            true
        }

        // Configuración del ActivityResultLauncher
        editFoodLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data ?: return@registerForActivityResult
                val id = data.getIntExtra("FOOD_ID", -1)
                val nombre = data.getStringExtra("FOOD_NAME") ?: return@registerForActivityResult
                val calorias = data.getIntExtra("FOOD_CALORIES", 0)
                val imagenUrl = data.getStringExtra("FOOD_IMAGE_URL")

                val updatedAlimento = Alimento(nombre, calorias, null, null, null, imagenUrl, "")
                viewModel.updateAlimento(id, updatedAlimento, {
                    adapter.notifyItemChanged(id)
                    Toast.makeText(this, "Alimento actualizado", Toast.LENGTH_SHORT).show()
                }, { error ->
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                })
            }
        }

        adapter = AlimentoAdapter(viewModel.alimentos, { position ->
            val alimento = viewModel.alimentos[position]
            val intent = Intent(this, EditFoodActivity::class.java).apply {
                putExtra("FOOD_ID", position)
                putExtra("FOOD_NAME", alimento.nombre)
                putExtra("FOOD_CALORIES", alimento.calorias.toString())
                putExtra("FOOD_IMAGE_URL", alimento.imagenUrl)
            }
            editFoodLauncher.launch(intent)
        }, { position ->
            viewModel.deleteAlimento(position, {
                adapter.notifyItemRemoved(position)
                Toast.makeText(this, "Alimento eliminado", Toast.LENGTH_SHORT).show()
            }, { error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            })
        })

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