package com.example.weather.currentweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import coil.load
import com.example.weather.ForecastRepository
import com.example.weather.Location
import com.example.weather.LocationRepository
import com.example.weather.api.OneCallForecast
import com.example.weather.databinding.FragmentCurrentWeatherBinding

class CurrentWeatherFragment : Fragment() {

    private var _binding: FragmentCurrentWeatherBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationRepository: LocationRepository
    private lateinit var forecastRepository: ForecastRepository

    private val viewModel = CurrentWeatherViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)

        locationRepository = LocationRepository(requireContext())
        forecastRepository = ForecastRepository()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val locationObserver = Observer<Location> { savedLocation ->
            when (savedLocation) {
                is Location.City ->  {
                    forecastRepository.loadOneCallData(savedLocation.name)
                    viewModel.location = savedLocation.name
                }
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, locationObserver)

        val forecastObserver = Observer<OneCallForecast> { oneCallForecast ->
            viewModel.manageData(oneCallForecast.current)
        }
        forecastRepository.oneCallForecast.observe(viewLifecycleOwner, forecastObserver)

        val viewStateObserver = Observer<CurrentWeatherViewState> { currentWeather ->
            binding.locationTextView.text = currentWeather.location
            binding.tempTextView.text = currentWeather.temp
            binding.mainTextView.text = currentWeather.description
            binding.iconImageView.load(currentWeather.iconUrl)
        }
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}