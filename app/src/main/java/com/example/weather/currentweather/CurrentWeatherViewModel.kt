package com.example.weather.currentweather

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weather.api.CurrentWeather

class CurrentWeatherViewModel : ViewModel() {

    private val _viewState: MutableLiveData<CurrentWeatherViewState> = MutableLiveData()
    val viewState: LiveData<CurrentWeatherViewState> = _viewState

    lateinit var location: String

    fun manageData(currentWeather: CurrentWeather) {
        _viewState.value = CurrentWeatherViewState(
            location,
            String.format("%.0f°", currentWeather.temp),
            currentWeather.weather[0].main,
            "http://openweathermap.org/img/wn/${currentWeather.weather[0].icon}@2x.png"
        )
    }

}