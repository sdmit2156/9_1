package com.example.weatherapp

import com.russhwolf.settings.Settings
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class WeatherRepository {
    private val api = WeatherApi()
    private val settings = Settings()
    private val json = Json { ignoreUnknownKeys = true }

    private val apiKey = "1b9f775f463e9cebee54c3b245b2d543"

    suspend fun getWeatherForCity(city: String): WeatherResponse {
        return try {
            val freshData = api.getWeather(city, apiKey)

            settings.putString("cached_weather_$city", json.encodeToString(freshData))
            freshData
        } catch (e: Exception) {
            val cachedJson = settings.getStringOrNull("cached_weather_$city")
            if (cachedJson != null) {
                json.decodeFromString<WeatherResponse>(cachedJson)
            } else {
                throw Exception("Нет интернета и нет сохраненных данных для $city. $e")
            }
        }
    }
}