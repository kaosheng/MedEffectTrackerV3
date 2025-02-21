package com.example.medeffecttracker

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class ChartActivity : AppCompatActivity() {

    private lateinit var lineChart: LineChart
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chart)

        dbHelper = DatabaseHelper(this)
        lineChart = findViewById(R.id.lineChart)

        setupChart()
        updateChartData()
    }

    private fun setupChart() {
        lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = DateAxisFormatter()
                labelRotationAngle = 45f
            }

            axisLeft.apply {
                axisMinimum = 0f
                setDrawGridLines(true)
            }

            axisRight.isEnabled = false
            legend.isEnabled = true
        }
    }

    private fun updateChartData() {
        val records = dbHelper.getAllRecords()

        // Process data for each period
        val dataSets = mutableListOf<ILineDataSet>() // Changed to ILineDataSet

        // Calculate time differences and create datasets
        val periodColors = mapOf(
            Record.PERIOD_MORNING to Color.BLUE,
            Record.PERIOD_NOON to Color.GREEN,
            Record.PERIOD_AFTERNOON to Color.RED,
            Record.PERIOD_NIGHT to Color.GRAY
        )

        periodColors.forEach { (period, color) ->
            val entries = calculateTimeDifferences(records, period)
            if (entries.isNotEmpty()) {
                val dataSet = LineDataSet(entries, period).apply {
                    this.color = color
                    circleColors = listOf(color)
                    lineWidth = 2f
                    circleRadius = 4f
                    setDrawValues(false)
                }
                dataSets.add(dataSet)
            }
        }

        lineChart.data = LineData(dataSets)
        lineChart.invalidate()
    }

    private fun calculateTimeDifferences(records: List<Record>, period: String): List<Entry> {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val entries = mutableListOf<Entry>()

        // Group records by date
        val recordsByDate = records.filter { it.period == period }
            .groupBy { it.date }

        recordsByDate.forEach { (date, dailyRecords) ->
            val timeRecord = dailyRecords.find { it.type == Record.TYPE_TIME }
            val startRecord = dailyRecords.find { it.type == Record.TYPE_START }
            val endRecord = dailyRecords.find { it.type == Record.TYPE_END }

            if (timeRecord != null && startRecord != null) {
                val timeDate = timeFormat.parse(timeRecord.time)
                val startDate = timeFormat.parse(startRecord.time)

                if (timeDate != null && startDate != null) {
                    val diffMinutes = (startDate.time - timeDate.time) / (1000 * 60)
                    entries.add(Entry(timeRecord.timestamp.toFloat(), diffMinutes.toFloat()))
                }
            }
        }

        return entries.sortedBy { it.x }
    }

    inner class DateAxisFormatter : ValueFormatter() {
        private val dateFormat = SimpleDateFormat("MM/dd", Locale.getDefault())

        override fun getFormattedValue(value: Float): String {
            return dateFormat.format(Date(value.toLong()))
        }
    }
}