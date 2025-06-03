package com.example.m07_p5

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.m07_p5.data.DataStoreManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.coroutines.launch

class StatisticsActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private lateinit var barChart: BarChart
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        lineChart = findViewById(R.id.line_chart)
        barChart = findViewById(R.id.bar_chart)
        dataStoreManager = DataStoreManager(this)

        setupCharts()
        observeDataStore()
    }

    private fun setupCharts() {
        // Configuración inicial del grafico con datos temporales
        lineChart.description.text = "Estadísticas de uso de la aplicación"
        barChart.description.text = "Acciones recientes"
    }

    private fun observeDataStore() {
        lifecycleScope.launch {
            dataStoreManager.usageCount.collect { count ->
                updateLineChart(count)
            }
        }

        lifecycleScope.launch {
            dataStoreManager.lastAction.collect { action ->
                updateBarChart(action)
            }
        }
    }

    private fun updateLineChart(count: Int) {
        val entries = mutableListOf<Entry>()
        for (i in 1..count) {
            entries.add(Entry(i.toFloat(), (i * 10).toFloat()))
        }

        val dataSet = LineDataSet(entries, "Sesiones")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataSet.valueTextSize = 12f

        lineChart.data = LineData(dataSet)
        lineChart.invalidate() // Para refrescar el gráfico
    }

    private fun updateBarChart(action: String) {
        val entries = listOf(
            BarEntry(1f, 10f),
            BarEntry(2f, 20f),
            BarEntry(3f, 30f)
        )

        val dataSet = BarDataSet(entries, "Acciones")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 12f

        barChart.data = BarData(dataSet)
        barChart.invalidate()
    }
}