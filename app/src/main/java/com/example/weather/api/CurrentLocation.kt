package com.example.weather.api

import com.squareup.moshi.Json

data class Coordinates(val lat: Float, val lon: Float)

data class CurrentLocation(
    val coord: Coordinates,
    val name: String
)
