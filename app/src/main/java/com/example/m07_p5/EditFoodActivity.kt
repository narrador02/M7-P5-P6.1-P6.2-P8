package com.example.m07_p5

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.m07_p5.data.network.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditFoodActivity : AppCompatActivity() {

    private lateinit var editFoodName: EditText
    private lateinit var editFoodQuantity: EditText
    private lateinit var editFoodDate: EditText
    private lateinit var editFoodImageUrl: EditText
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button

    private var foodId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_food)

        editFoodName = findViewById(R.id.edit_food_name)
        editFoodQuantity = findViewById(R.id.edit_food_quantity)
        editFoodDate = findViewById(R.id.edit_food_date)
        editFoodImageUrl = findViewById(R.id.edit_food_image_url)
        btnSave = findViewById(R.id.btn_save_food)
        btnDelete = findViewById(R.id.btn_delete_food)

        foodId = intent.getIntExtra("FOOD_ID", -1)
        val foodName = intent.getStringExtra("FOOD_NAME") ?: ""
        val foodQuantity = intent.getStringExtra("FOOD_CALORIES") ?: ""
        val foodDate = intent.getStringExtra("FOOD_DATE") ?: ""
        val foodImageUrl = intent.getStringExtra("FOOD_IMAGE_URL") ?: ""

        Log.d("EditFoodActivity", "Recibido: ID=$foodId, Nombre=$foodName, Cantidad=$foodQuantity, Fecha=$foodDate, Imagen=$foodImageUrl")

        if (foodId == -1) {
            Toast.makeText(this, "Error: No se recibieron datos", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        editFoodName.setText(foodName)
        editFoodQuantity.setText(foodQuantity)
        editFoodDate.setText(foodDate)
        editFoodImageUrl.setText(foodImageUrl)

        btnSave.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("FOOD_ID", foodId)
                putExtra("FOOD_NAME", editFoodName.text.toString())
                putExtra("FOOD_CALORIES", editFoodQuantity.text.toString().toInt())
                putExtra("FOOD_IMAGE_URL", editFoodImageUrl.text.toString())
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        btnDelete.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Eliminar alimento")
            .setMessage("¿Estás seguro de que quieres eliminar este alimento?")
            .setPositiveButton("Sí") { _, _ ->
                deleteFoodItem()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun deleteFoodItem() {
        val foodName = editFoodName.text.toString()
        if (foodName.isNotEmpty()) {
            lifecycleScope.launch {
                try {
                    withContext(Dispatchers.IO) {
                        RetrofitClient.apiService.deleteAlimentoByName(foodName)
                    }
                    Toast.makeText(this@EditFoodActivity, "Alimento eliminado", Toast.LENGTH_SHORT).show()
                    finish() // Cierra la actividad
                } catch (e: Exception) {
                    Toast.makeText(this@EditFoodActivity, "Error al eliminar: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Nombre del alimento vacío", Toast.LENGTH_SHORT).show()
        }
    }
}