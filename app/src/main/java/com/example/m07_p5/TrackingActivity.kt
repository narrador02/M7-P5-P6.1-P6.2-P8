package com.example.m07_p5

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray

class TrackingActivity : AppCompatActivity() {

    private lateinit var tvSummary: TextView
    private lateinit var btnUpdateData: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        tvSummary = findViewById(R.id.tv_summary)
        btnUpdateData = findViewById(R.id.btn_update_data)

        updateCalorieSummary()

        btnUpdateData.setOnClickListener {
            updateCalorieSummary()
        }
    }

    private fun updateCalorieSummary() {
        val sharedPref = getSharedPreferences("consumption_history", Context.MODE_PRIVATE)
        val jsonArray = JSONArray(sharedPref.getString("consumed_foods", "[]") ?: "[]")

        var totalCalories = 0

        for (i in 0 until jsonArray.length()) {
            val foodObject = jsonArray.getJSONObject(i)
            val caloriesString = foodObject.getString("calories").replace(" kcal", "").trim()
            val calories = caloriesString.toIntOrNull() ?: 0
            totalCalories += calories
        }

        tvSummary.text = "Total de calorías consumidas: $totalCalories kcal"
    }
}