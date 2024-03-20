package com.vikas.weatherapplication

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var cityEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton: Button = findViewById(R.id.searchButton)
        cityEditText = findViewById(R.id.cityInput)

        searchButton.setOnClickListener {
            val cityName = cityEditText.text.toString()
            val errorTexts = findViewById<TextView>(R.id.errorTexts)
            if (cityName.isNotEmpty()) {
                weatherTask().execute(cityName)
            } else {
                errorTexts.visibility = View.VISIBLE
                errorTexts.text = "Please enter a city name"
                Handler().postDelayed({
                    errorTexts.visibility = View.GONE
                }, 2000) // Hide error text after 2 seconds
            }
        }
    }

    inner class weatherTask : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                val cityName = params[0]
                val apiKey = "6caca6c6d80e29f571d8536b55684320" // Replace with your OpenWeatherMap API key
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$cityName&units=metric&appid=$apiKey").readText(
                    Charsets.UTF_8
                )
            } catch (e: Exception) {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt: Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt * 1000))
                val temp = main.getString("temp") + "°C"
                val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
                val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")

                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")

                val address = jsonObj.getString("name") + ", " + sys.getString("country")

                // Pass city details to SearchedCityActivity
                val intent = Intent(this@MainActivity, SearchedCityActivity::class.java).apply {
                    putExtra("address", address)
                    putExtra("updatedAt", updatedAtText)
                    putExtra("weatherDescription", weatherDescription.capitalize())
                    putExtra("temp", temp)
                    putExtra("tempMin", tempMin)
                    putExtra("tempMax", tempMax)
                    putExtra("sunrise", SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000)))
                    putExtra("sunset", SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000)))
                    putExtra("windSpeed", "$windSpeed m/s")
                    putExtra("pressure", "$pressure hPa")
                    putExtra("humidity", "$humidity %")
                }

                // Start SearchedCityActivity
                startActivity(intent)
            } catch (e: Exception) {
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<TextView>(R.id.errorText).visibility = View.VISIBLE
                findViewById<TextView>(R.id.errorTexts).visibility = View.GONE

                // Hide error text after 3 seconds
                val errorText = findViewById<TextView>(R.id.errorText)
                errorText.visibility = View.VISIBLE
                errorText.text = "Something went wrong"
                Handler().postDelayed({
                    errorText.visibility = View.GONE
                    // Navigate back to MainActivity
                    startActivity(Intent(this@MainActivity, MainActivity::class.java))
                    finish()
                }, 3000)

            }
        }
    }
}
