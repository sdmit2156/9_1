package com.example.weatherapp

import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val name: String,
    val main: MainInfo,
    val weather: List<WeatherInfo>,
    val wind: WindInfo
)

@Serializable
data class MainInfo(
    val temp: Double,
    val humidity: Int
)

@Serializable
data class WeatherInfo(
    val description: String,
    val icon: String
)

@Serializable
data class WindInfo(
    val speed: Double
)