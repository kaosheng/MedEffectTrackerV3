package com.example.medeffecttracker


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    private val correctPassword = "74042569122507200112"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnDonate = findViewById<Button>(R.id.btnDonate)

        btnDonate.setOnClickListener {
            if (etPassword.text.toString() == correctPassword) {
                // Open donation link
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://your-donation-link.com"))
                startActivity(intent)
            } else {
                Toast.makeText(this, "密碼錯誤", Toast.LENGTH_SHORT).show()
            }
        }
    }
}