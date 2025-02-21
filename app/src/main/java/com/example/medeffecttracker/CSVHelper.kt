package com.example.medeffecttracker

import android.content.Context
import android.os.Environment
import java.io.*

class CSVHelper(private val context: Context) {

    fun exportToCSV(): String {
        val dbHelper = DatabaseHelper(context)
        val records = dbHelper.getAllRecords()

        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "med_effect.csv")
        val writer = BufferedWriter(FileWriter(file))

        writer.write("日期,時間,時段,類型\n")
        records.forEach {
            writer.write("${it.date},${it.time},${it.period},${it.type}\n")
        }

        writer.flush()
        writer.close()
        return file.absolutePath
    }
}
