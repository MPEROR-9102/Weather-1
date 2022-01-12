package com.example.weather.api

data class Coordinates(val lat: Double, val lon: Double)

data class CurrentLocation(
    val coord: Coordinates,
    val name: String
)
