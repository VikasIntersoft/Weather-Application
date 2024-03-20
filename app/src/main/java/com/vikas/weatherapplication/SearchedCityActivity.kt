package com.vikas.weatherapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SearchedCityActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searched_city)

        // Get data passed from MainActivity
        val address = intent.getStringExtra("address") ?: ""
        val updatedAt = intent.getStringExtra("updatedAt") ?: ""
        val weatherDescription = intent.getStringExtra("weatherDescription") ?: ""
        val temp = intent.getStringExtra("temp") ?: ""
        val tempMin = intent.getStringExtra("tempMin") ?: ""
        val tempMax = intent.getStringExtra("tempMax") ?: ""
        val sunrise = intent.getStringExtra("sunrise") ?: ""
        val sunset = intent.getStringExtra("sunset") ?: ""
        val windSpeed = intent.getStringExtra("windSpeed") ?: ""
        val pressure = intent.getStringExtra("pressure") ?: ""
        val humidity = intent.getStringExtra("humidity") ?: ""

        // Set data to views
        findViewById<TextView>(R.id.address).text = address
        findViewById<TextView>(R.id.updated_at).text = updatedAt
        findViewById<TextView>(R.id.status).text = weatherDescription
        findViewById<TextView>(R.id.temp).text = temp
        findViewById<TextView>(R.id.temp_min).text = tempMin
        findViewById<TextView>(R.id.temp_max).text = tempMax
        findViewById<TextView>(R.id.sunrise).text = sunrise
        findViewById<TextView>(R.id.sunset).text = sunset
        findViewById<TextView>(R.id.wind).text = windSpeed
        findViewById<TextView>(R.id.pressure).text = pressure
        findViewById<TextView>(R.id.humidity).text = humidity
    }
        override fun onBackPressed() {
            super.onBackPressed()
            // Navigate back to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

}
