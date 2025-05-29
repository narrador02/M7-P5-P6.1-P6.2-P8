package com.example.m07_p5

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.*
import org.json.JSONArray
import org.json.JSONObject

class AddConsumptionActivity : BaseActivity() {

    private lateinit var editFoodName: EditText
    private lateinit var editFoodCalories: EditText
    private lateinit var btnSave: Button
    private lateinit var btnSelectDate: Button
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_consumption)

        editFoodName = findViewById(R.id.edit_food_name)
        editFoodCalories = findViewById(R.id.edit_food_calories)
        btnSave = findViewById(R.id.btn_save_food)
        btnSelectDate = findViewById(R.id.btn_select_date)

        btnSelectDate.setOnClickListener {
            showDatePickerDialog()
        }

        btnSave.setOnClickListener {
            saveConsumption()
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
                btnSelectDate.text = selectedDate
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun saveConsumption() {
        val foodName = editFoodName.text.toString().trim()
        val foodCalories = editFoodCalories.text.toString().trim()

        if (foodName.isBlank() || foodCalories.isBlank() || selectedDate.isNullOrEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPref = getSharedPreferences("consumption_history", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        val jsonArray = JSONArray(sharedPref.getString("consumed_foods", "[]") ?: "[]")
        val newFood = JSONObject().apply {
            put("name", foodName)
            put("calories", "$foodCalories kcal")
            put("date", selectedDate)
        }

        jsonArray.put(newFood)
        editor.putString("consumed_foods", jsonArray.toString())
        editor.apply()

        Toast.makeText(this, "Alimento agregado al historial", Toast.LENGTH_SHORT).show()
        finish()
    }
}