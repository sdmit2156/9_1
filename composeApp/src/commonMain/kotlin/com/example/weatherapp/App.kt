package com.example.weatherapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    MaterialTheme {
        val viewModel = remember { WeatherViewModel() }
        val state by viewModel.state.collectAsState()

        var city by remember { mutableStateOf("") }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Кроссплатформенная погода",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = city,
                    onValueChange = { city = it },
                    label = { Text("Введите название города") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { viewModel.fetchWeather(city) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Узнать погоду")
                }

                Spacer(modifier = Modifier.height(24.dp))

                when (val currentState = state) {
                    is WeatherState.Idle -> {
                        Text("Введите город и нажмите кнопку для поиска.")
                    }
                    is WeatherState.Loading -> {
                        CircularProgressIndicator()
                    }
                    is WeatherState.Success -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = currentState.data.name,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("Температура: ${currentState.data.main.temp} °C")
                                Text("Влажность: ${currentState.data.main.humidity}%")
                                Text("Скорость ветра: ${currentState.data.wind.speed} м/с")

                                if (currentState.data.weather.isNotEmpty()) {
                                    Text("Описание: ${currentState.data.weather[0].description}")
                                }
                            }
                        }
                    }
                    is WeatherState.Error -> {
                        Text(
                            text = "Ошибка: ${currentState.message}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}