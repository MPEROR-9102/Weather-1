package com.example.weather.currentweather

data class CurrentWeatherViewState(
    val location: String,
    val temp: String,
    val description: String,
    val iconUrl: String
)