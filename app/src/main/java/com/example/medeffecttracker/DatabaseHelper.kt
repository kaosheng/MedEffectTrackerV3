package com.example.medeffecttracker

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE records (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                date TEXT,
                time TEXT,
                period TEXT,
                type TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS records")
        onCreate(db)
    }

    fun insertRecord(date: String, time: String, period: String, type: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("date", date)
            put("time", time)
            put("period", period)
            put("type", type)
        }
        db.insert("records", null, values)
        db.close()
    }

    fun getAllRecords(): List<Record> {
        val records = mutableListOf<Record>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM records ORDER BY date, time", null)
        while (cursor.moveToNext()) {
            records.add(
                Record(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4)
                )
            )
        }
        cursor.close()
        db.close()
        return records
    }

    companion object {
        private const val DATABASE_NAME = "MedEffectTracker.db"
        private const val DATABASE_VERSION = 1
    }
}
