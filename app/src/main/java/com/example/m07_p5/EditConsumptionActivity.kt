package com.example.m07_p5

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class EditConsumptionActivity : AppCompatActivity() {

    private lateinit var editFoodName: EditText
    private lateinit var editFoodCalories: EditText
    private lateinit var editFoodDate: EditText
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button

    private var foodId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_food)

        editFoodName = findViewById(R.id.edit_food_name)
        editFoodCalories = findViewById(R.id.edit_food_quantity)
        editFoodDate = findViewById(R.id.edit_food_date)
        btnSave = findViewById(R.id.btn_save_food)
        btnDelete = findViewById(R.id.btn_delete_food)

        foodId = intent.getIntExtra("FOOD_ID", -1)
        val foodName = intent.getStringExtra("FOOD_NAME") ?: ""
        val foodCalories = intent.getStringExtra("FOOD_CALORIES") ?: ""
        val foodDate = intent.getStringExtra("FOOD_DATE") ?: ""

        if (foodId == -1) {
            Toast.makeText(this, "Error: No se recibieron datos", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        editFoodName.setText(foodName)
        editFoodCalories.setText(foodCalories.replace(" kcal", ""))
        editFoodDate.setText(foodDate)

        btnSave.setOnClickListener {
            saveChanges()
        }

        btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun saveChanges() {
        val sharedPref = getSharedPreferences("consumption_history", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val jsonArray = JSONArray(sharedPref.getString("consumed_foods", "[]") ?: "[]")

        if (foodId in 0 until jsonArray.length()) {
            val foodObject = jsonArray.getJSONObject(foodId)
            foodObject.put("name", editFoodName.text.toString().trim())
            foodObject.put("calories", "${editFoodCalories.text.toString().trim()} kcal")
            foodObject.put("date", editFoodDate.text.toString().trim())

            jsonArray.put(foodId, foodObject)
            editor.putString("consumed_foods", jsonArray.toString())
            editor.apply()

            Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al guardar los cambios", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar alimento")
            .setMessage("¿Estás seguro de que quieres eliminar este alimento del historial?")
            .setPositiveButton("Sí") { _, _ ->
                deleteFoodItem()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteFoodItem() {
        val sharedPref = getSharedPreferences("consumption_history", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val jsonArray = JSONArray(sharedPref.getString("consumed_foods", "[]") ?: "[]")

        if (foodId in 0 until jsonArray.length()) {
            val updatedArray = JSONArray()
            for (i in 0 until jsonArray.length()) {
                if (i != foodId) {
                    updatedArray.put(jsonArray.getJSONObject(i))
                }
            }

            editor.putString("consumed_foods", updatedArray.toString())
            editor.apply()

            Toast.makeText(this, "Alimento eliminado", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al eliminar el alimento", Toast.LENGTH_SHORT).show()
        }
    }
}