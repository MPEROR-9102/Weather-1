package com.example.weather

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

sealed class Location {
    data class City(val name: String) : Location()
}

private const val KEY_CITY = "key_city"

class LocationRepository(context: Context) {

    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    private val forecastRepository = ForecastRepository()

    private val _savedLocation: MutableLiveData<Location> = MutableLiveData()
    val savedLocation: LiveData<Location> = _savedLocation

    init {
        preferences.registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key != KEY_CITY) return@registerOnSharedPreferenceChangeListener
            broadcastSavedLocation()
        }
        broadcastSavedLocation()
    }

    fun saveLocation(cityName: String) {
        preferences.edit().putString(KEY_CITY, cityName).apply()
    }

    private fun broadcastSavedLocation() {
        val city = preferences.getString(KEY_CITY, "")
        if (city != null && city.isNotBlank()) {
            _savedLocation.value = Location.City(city)
        }
    }
}