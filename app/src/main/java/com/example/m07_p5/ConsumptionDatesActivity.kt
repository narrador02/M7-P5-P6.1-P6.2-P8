package com.example.m07_p5

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONArray
import java.util.*

class ConsumptionDatesActivity : BaseActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ConsumptionAdapter
    private lateinit var fabAddFood: FloatingActionButton
    private lateinit var btnDatePicker: Button
    private lateinit var tvSelectedDate: TextView
    private val consumptionList = mutableListOf<ConsumptionItem>()
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumption_dates)

        setupBottomNavigation(R.id.bottom_navigation, R.id.nav_dates)

        recyclerView = findViewById(R.id.recycler_view)
        fabAddFood = findViewById(R.id.fab_add_food)
        btnDatePicker = findViewById(R.id.btn_date_picker)
        tvSelectedDate = findViewById(R.id.tv_selected_date)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ConsumptionAdapter(consumptionList) { item, position ->
            val intent = Intent(this, EditConsumptionActivity::class.java).apply {
                putExtra("FOOD_ID", position)
                putExtra("FOOD_NAME", item.name)
                putExtra("FOOD_CALORIES", item.calories)
                putExtra("FOOD_DATE", item.date)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        btnDatePicker.setOnClickListener {
            showDatePickerDialog()
        }

        fabAddFood.setOnClickListener {
            val intent = Intent(this, AddConsumptionActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        updateList()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year)
                tvSelectedDate.text = "Fecha: $selectedDate"
                updateList()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun updateList() {
        consumptionList.clear()
        val sharedPref = getSharedPreferences("consumption_history", Context.MODE_PRIVATE)
        val jsonArray = JSONArray(sharedPref.getString("consumed_foods", "[]") ?: "[]")

        for (i in 0 until jsonArray.length()) {
            val foodObject = jsonArray.getJSONObject(i)
            val food = ConsumptionItem(
                foodObject.getString("name"),
                foodObject.getString("calories"),
                foodObject.getString("date")
            )
            if (selectedDate == null || food.date == selectedDate) {
                consumptionList.add(food)
            }
        }

        adapter.notifyDataSetChanged()
    }
}