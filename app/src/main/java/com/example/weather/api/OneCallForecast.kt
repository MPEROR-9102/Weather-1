package com.example.weather.api

import com.squareup.moshi.Json

data class Temperature(
    val min: Float,
    val max: Float
)

data class WeatherDescription(
    val main: String,
    val description: String,
    val icon: String
)

data class CurrentWeather(
    @field: Json(name = "dt") val date: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Float,
    val pressure: Int,
    val humidity: Int,
    val visibility: Int,
    val wind_speed: Float,
    val wind_deg: Int,
    val weather: List<WeatherDescription>
)

data class HourlyForecast(
    @field: Json(name = "dt") val date: Long,
    val temp: Float,
    val weather: List<WeatherDescription>
)

data class DailyForecast(
    @field: Json(name = "dt") val date: Long,
    val temp: Temperature,
    val humidity: Int,
    val wind_speed: Float,
    val wind_deg: Int,
    val weather: List<WeatherDescription>
)

data class OneCallForecast(
    val timezone: String,
    var name: String,
    val current: CurrentWeather,
    val hourly: List<HourlyForecast>,
    val daily: List<DailyForecast>
)
