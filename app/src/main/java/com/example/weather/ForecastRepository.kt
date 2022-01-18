package com.example.weather

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weather.api.CurrentLocation
import com.example.weather.api.OneCallForecast
import com.example.weather.api.createOpenWeatherMapService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForecastRepository {

    private val _oneCallForecast: MutableLiveData<OneCallForecast> = MutableLiveData()
    val oneCallForecast: LiveData<OneCallForecast> = _oneCallForecast

    fun loadOneCallData(cityName: String) {
        val locationCall = createOpenWeatherMapService().currentLocation(
            cityName,
            BuildConfig.OPEN_WEATHER_MAP_API_KEY
        )
        locationCall.enqueue(object : Callback<CurrentLocation> {
            override fun onResponse(
                call: Call<CurrentLocation>,
                response: Response<CurrentLocation>
            ) {
                val currentLocationResponse = response.body()

                if (currentLocationResponse != null) {
                    val forecastCall = createOpenWeatherMapService().oneCallForecast(
                        currentLocationResponse.coord.lat,
                        currentLocationResponse.coord.lon,
                        BuildConfig.OPEN_WEATHER_MAP_API_KEY,
                        "minutely,alerts",
                        "imperial"
                    )
                    forecastCall.enqueue(object : Callback<OneCallForecast> {
                        override fun onResponse(
                            call: Call<OneCallForecast>,
                            response: Response<OneCallForecast>
                        ) {
                            val forecastResponse = response.body()
                            if (forecastResponse != null) {
                                forecastResponse.name = currentLocationResponse.name
                                forecastResponse.country = currentLocationResponse.sys.country
                                _oneCallForecast.value = forecastResponse
                            }
                        }

                        override fun onFailure(call: Call<OneCallForecast>, t: Throwable) {
                            Log.e(
                                ForecastRepository::class.java.simpleName,
                                "Error loading forecast details",
                                t
                            )
                        }
                    })
                }

            }

            override fun onFailure(call: Call<CurrentLocation>, t: Throwable) {
                Log.e(
                    ForecastRepository::class.java.simpleName,
                    "Error loading location details",
                    t
                )
            }
        })
    }
}