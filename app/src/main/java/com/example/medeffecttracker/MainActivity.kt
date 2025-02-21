package com.example.medeffecttracker

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        setupButton(R.id.btnMorningMed, "morning", "time")
        setupButton(R.id.btnMorningStart, "morning", "start")
        setupButton(R.id.btnMorningEnd, "morning", "end")

        setupButton(R.id.btnNoonMed, "noon", "time")
        setupButton(R.id.btnNoonStart, "noon", "start")
        setupButton(R.id.btnNoonEnd, "noon", "end")

        setupButton(R.id.btnAfternoonMed, "afternoon", "time")
        setupButton(R.id.btnAfternoonStart, "afternoon", "start")
        setupButton(R.id.btnAfternoonEnd, "afternoon", "end")

        setupButton(R.id.btnNightMed, "night", "time")
        setupButton(R.id.btnNightStart, "night", "start")
        setupButton(R.id.btnNightEnd, "night", "end")

        findViewById<Button>(R.id.btnExportCSV).setOnClickListener {
            val csvHelper = CSVHelper(this)
            val filePath = csvHelper.exportToCSV()
            Toast.makeText(this, "CSV 匯出成功: $filePath", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupButton(buttonId: Int, period: String, type: String) {
        findViewById<Button>(buttonId).setOnClickListener {
            val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = dateFormat.format(Date())
            val time = timeFormat.format(Date())

            dbHelper.insertRecord(date, time, period, type)
            Toast.makeText(this, "記錄成功: $date $time - $period - $type", Toast.LENGTH_SHORT).show()
        }
    }
}
