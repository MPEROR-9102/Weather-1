package com.example.weather.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

fun createOpenWeatherMapService(): OpenWeatherMapService {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    return retrofit.create(OpenWeatherMapService::class.java)
}

interface OpenWeatherMapService {

    @GET("/data/2.5/weather")
    fun currentLocation(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String
    ): Call<CurrentLocation>

    @GET("/data/2.5/onecall")
    fun oneCallForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("exclude") exclude: String,
        @Query("units") units: String
    ): Call<OneCallForecast>
}