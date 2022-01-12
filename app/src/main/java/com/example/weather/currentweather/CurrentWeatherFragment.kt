package com.example.weather.currentweather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.weather.*
import com.example.weather.api.OneCallForecast
import com.example.weather.databinding.FragmentCurrentWeatherBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CurrentWeatherFragment : Fragment() {

    private var _binding: FragmentCurrentWeatherBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationRepository: LocationRepository
    private lateinit var forecastRepository: ForecastRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)
        activity?.findViewById<FloatingActionButton>(R.id.currentLocationButton)?.show()

        locationRepository = LocationRepository(requireContext())
        forecastRepository = ForecastRepository()

        val hourlyForecastAdapter = HourlyForecastAdapter()
        binding.hourlyForecastRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.hourlyForecastRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
        binding.hourlyForecastRecyclerView.adapter = hourlyForecastAdapter

        val locationObserver = Observer<Location> { savedLocation ->
            when (savedLocation) {
                is Location.City ->  {
                    forecastRepository.loadOneCallData(savedLocation.name)
                }
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, locationObserver)

        val forecastObserver = Observer<OneCallForecast> { oneCallForecast ->

            binding.currentWeatherProgressBar.visibility = ProgressBar.GONE

            binding.hourlyForecastCardView.visibility = ImageView.VISIBLE
            binding.arrowUpImage.visibility = ImageView.VISIBLE
            binding.sunriseImageView.visibility = ImageView.VISIBLE
            binding.sunsetImageView.visibility = ImageView.VISIBLE
            binding.arrowDownImage.visibility = ImageView.VISIBLE
            binding.windSpeedImage.visibility = ImageView.VISIBLE
            binding.humidityText.visibility = ImageView.VISIBLE
            binding.windDegreeText.visibility = ImageView.VISIBLE
            binding.visibilityText.visibility = ImageView.VISIBLE

            binding.locationTextView.text = locationRepository.getLocation()
            binding.dateTextView.text = formatDateTime(oneCallForecast.current.date, FormatType.Date)
            binding.tempTextView.text = formatTempDisplay(oneCallForecast.current.temp)
            binding.mainTextView.text = oneCallForecast.current.weather[0].main
            binding.iconImageView.load(iconUrl(oneCallForecast.current.weather[0].icon))

            hourlyForecastAdapter.submitList(oneCallForecast.hourly.subList(0,24))

            binding.sunriseTextView.text = formatDateTime(oneCallForecast.current.sunrise, FormatType.SunHour)
            binding.sunsetTextView.text = formatDateTime(oneCallForecast.current.sunset, FormatType.SunHour)
            binding.windSpeedTextView.text = String.format("%1$.1f km/h", oneCallForecast.current.wind_speed)
            binding.humidityTextView.text = oneCallForecast.current.humidity.toString()
            binding.windDegreeTextView.text = oneCallForecast.current.wind_deg.toString()
            binding.visibilityTextView.text = String.format("${oneCallForecast.current.visibility/1000} km")
        }
        forecastRepository.oneCallForecast.observe(viewLifecycleOwner, forecastObserver)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}