package com.example.m07_p5

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class EditFoodActivity : AppCompatActivity() {

    private lateinit var editFoodName: EditText
    private lateinit var editFoodQuantity: EditText
    private lateinit var editFoodDate: EditText
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button

    private var foodId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_food)

        editFoodName = findViewById(R.id.edit_food_name)
        editFoodQuantity = findViewById(R.id.edit_food_quantity)
        editFoodDate = findViewById(R.id.edit_food_date)
        btnSave = findViewById(R.id.btn_save_food)
        btnDelete = findViewById(R.id.btn_delete_food)

        foodId = intent.getIntExtra("FOOD_ID", -1)
        val foodName = intent.getStringExtra("FOOD_NAME") ?: ""
        val foodQuantity = intent.getStringExtra("FOOD_QUANTITY") ?: ""
        val foodDate = intent.getStringExtra("FOOD_DATE") ?: ""

        Log.d("EditFoodActivity", "Recibido: ID=$foodId, Nombre=$foodName, Cantidad=$foodQuantity, Fecha=$foodDate")

        if (foodId == -1) {
            Toast.makeText(this, "Error: No se recibieron datos", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        editFoodName.setText(foodName)
        editFoodQuantity.setText(foodQuantity)
        editFoodDate.setText(foodDate)

        btnSave.setOnClickListener {
            saveFoodItem()
        }

        btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun saveFoodItem() {
        val sharedPref = getSharedPreferences("food_list", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        val jsonArray = JSONArray(sharedPref.getString("foods", "[]") ?: "[]")

        if (foodId in 0 until jsonArray.length()) {
            val updatedFood = jsonArray.getJSONObject(foodId)
            updatedFood.put("name", editFoodName.text.toString())
            updatedFood.put("quantity", editFoodQuantity.text.toString())
            updatedFood.put("date", editFoodDate.text.toString())

            editor.putString("foods", jsonArray.toString())
            editor.apply()
        }

        Toast.makeText(this, "Cambios guardados", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar alimento")
            .setMessage("¿Estás seguro de que quieres eliminar este alimento?")
            .setPositiveButton("Sí") { _, _ ->
                val sharedPref = getSharedPreferences("food_list", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                val jsonArray = JSONArray(sharedPref.getString("foods", "[]") ?: "[]")

                if (foodId in 0 until jsonArray.length()) {
                    jsonArray.remove(foodId)
                    editor.putString("foods", jsonArray.toString())
                    editor.apply()
                }

                Toast.makeText(this, "Alimento eliminado", Toast.LENGTH_SHORT).show()
                finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
}