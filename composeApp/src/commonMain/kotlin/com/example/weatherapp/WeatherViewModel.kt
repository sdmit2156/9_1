package com.example.weatherapp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class WeatherState {
    object Idle : WeatherState()
    object Loading : WeatherState()
    data class Success(val data: WeatherResponse) : WeatherState()
    data class Error(val message: String) : WeatherState()
}

class WeatherViewModel {
    private val repository = WeatherRepository()
    private val viewModelScope = CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow<WeatherState>(WeatherState.Idle)
    val state: StateFlow<WeatherState> = _state.asStateFlow()

    fun fetchWeather(city: String) {
        if (city.isBlank()) return

        viewModelScope.launch {
            _state.value = WeatherState.Loading
            try {
                val weather = repository.getWeatherForCity(city)
                _state.value = WeatherState.Success(weather)
            } catch (e: Exception) {
                _state.value = WeatherState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }
}