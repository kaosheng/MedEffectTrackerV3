package com.example.medeffecttracker

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HistoryActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        dbHelper = DatabaseHelper(this)

        val recordsView = findViewById<TextView>(R.id.recordsView)
        val records = dbHelper.getAllRecords() // 讀取所有紀錄

        val recordsText = StringBuilder()
        if (records.isEmpty()) {
            recordsText.append("⚠️ 沒有資料，請先新增或匯入 CSV！")
        } else {
            for (record in records) {
                recordsText.append("${record.date} ${record.time} | ${record.period} - ${record.type}\n")
            }
        }

        recordsView.text = recordsText.toString()
    }
}
