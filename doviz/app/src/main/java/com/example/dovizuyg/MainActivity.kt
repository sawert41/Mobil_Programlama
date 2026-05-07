package com.example.dovizuyg

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

class MainActivity : Activity() {

    private lateinit var chfRateText: TextView
    private lateinit var usdRateText: TextView
    private lateinit var jpyRateText: TextView
    private lateinit var tryRateText: TextView
    private lateinit var cadRateText: TextView
    private lateinit var button: Button

    private val apiUrl: String
        get() = "http://data.fixer.io/api/latest?access_key=${BuildConfig.FIXER_API_KEY}"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chfRateText = findViewById(R.id.chfRateText)
        usdRateText = findViewById(R.id.usdRateText)
        jpyRateText = findViewById(R.id.jpyRateText)
        tryRateText = findViewById(R.id.tryRateText)
        cadRateText = findViewById(R.id.cadRateText)
        button = findViewById(R.id.button)

        button.setOnClickListener {
            getRates()
        }
    }

    private fun getRates() {
        button.isEnabled = false
        setAllTexts("Yükleniyor...")

        Thread {
            var connection: HttpURLConnection? = null

            try {
                if (BuildConfig.FIXER_API_KEY.isBlank()) {
                    throw Exception("FIXER_API_KEY local.properties icinde bos")
                }

                val url = URL(apiUrl)
                connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 15000
                connection.readTimeout = 15000
                connection.useCaches = false
                connection.setRequestProperty("Accept", "application/json")
                connection.setRequestProperty("Connection", "close")

                val responseCode = connection.responseCode
                val inputStream = if (responseCode in 200..299) {
                    connection.inputStream
                } else {
                    connection.errorStream
                }

                val response = inputStream?.bufferedReader()?.use { it.readText() }.orEmpty()
                if (response.isBlank()) {
                    throw Exception("Bos API cevabi: HTTP $responseCode")
                }

                val jsonObject = JSONObject(response)

                if (jsonObject.has("success") && !jsonObject.optBoolean("success")) {
                    val errorMessage = if (jsonObject.has("error")) {
                        val error = jsonObject.getJSONObject("error")
                        val code = error.optInt("code", -1)
                        val info = error.optString("info", "API hatası")
                        "API $code: $info"
                    } else {
                        "API hatası"
                    }
                    throw Exception(errorMessage)
                }

                val rates = jsonObject.getJSONObject("rates")

                val chf = rates.getDouble("CHF")
                val usd = rates.getDouble("USD")
                val jpy = rates.getDouble("JPY")
                val tryRate = rates.getDouble("TRY")
                val cad = rates.getDouble("CAD")

                runOnUiThread {
                    chfRateText.text = formatRate(chf)
                    usdRateText.text = formatRate(usd)
                    jpyRateText.text = formatRate(jpy)
                    tryRateText.text = formatRate(tryRate)
                    cadRateText.text = formatRate(cad)
                    button.isEnabled = true
                }

            } catch (e: Exception) {
                Log.e("MainActivity", "Kur bilgileri alinamadi", e)
                runOnUiThread {
                    chfRateText.text = "Hata"
                    usdRateText.text = "Bağlantı/API"
                    jpyRateText.text = "kontrol"
                    tryRateText.text = "edin"
                    cadRateText.text = e.message ?: "Bilinmeyen hata"
                    button.isEnabled = true
                }
            } finally {
                connection?.disconnect()
            }
        }.start()
    }

    private fun setAllTexts(text: String) {
        chfRateText.text = text
        usdRateText.text = text
        jpyRateText.text = text
        tryRateText.text = text
        cadRateText.text = text
    }

    private fun formatRate(value: Double): String {
        return String.format(Locale.US, "%.4f", value)
    }
}
