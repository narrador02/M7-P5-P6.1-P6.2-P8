package com.example.m07_p5

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.json.JSONArray
import org.json.JSONObject

class AddFoodActivity : BaseActivity() {

    private lateinit var foodNameInput: EditText
    private lateinit var foodQuantityInput: EditText
    private lateinit var foodDateInput: EditText
    private lateinit var btnAddFood: Button
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        setupBottomNavigation(R.id.bottom_navigation, R.id.nav_add_food)

        try {
            foodNameInput = findViewById(R.id.edit_food_name)
            foodQuantityInput = findViewById(R.id.edit_food_quantity)
            foodDateInput = findViewById(R.id.edit_food_date)
            btnAddFood = findViewById(R.id.btn_save_food)
            bottomNav = findViewById(R.id.bottom_navigation)

            btnAddFood.setOnClickListener {
                val foodName = foodNameInput.text.toString()
                val foodQuantity = foodQuantityInput.text.toString()
                val foodDate = foodDateInput.text.toString()

                if (foodName.isNotEmpty() && foodQuantity.isNotEmpty() && foodDate.isNotEmpty()) {
                    saveFoodItem(foodName, foodQuantity, foodDate)
                    Toast.makeText(this, "Alimento añadido", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("AddFoodActivity", "Error en la inicialización", e)
        }
    }

    private fun saveFoodItem(name: String, quantity: String, date: String) {
        val sharedPref = getSharedPreferences("food_list", MODE_PRIVATE)
        val editor = sharedPref.edit()

        val foodListString = sharedPref.getString("foods", "[]") ?: "[]"
        val foodList = JSONArray(foodListString)

        val newFood = JSONObject().apply {
            put("name", name)
            put("quantity", quantity)
            put("date", date)
        }

        foodList.put(newFood)
        editor.putString("foods", foodList.toString())
        editor.apply()
    }
}
