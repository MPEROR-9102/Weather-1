package com.example.weather.currentweather

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
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
    private lateinit var settingsManager: SettingsManager

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCurrentWeatherBinding.inflate(inflater, container, false)

        activity?.findViewById<FloatingActionButton>(R.id.currentLocationButton)?.show()

        locationRepository = LocationRepository(requireContext())
        forecastRepository = ForecastRepository()
        settingsManager = SettingsManager(requireContext())

        val hourlyForecastAdapter = HourlyForecastAdapter(settingsManager)
        binding.hourlyForecastRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.hourlyForecastRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.HORIZONTAL
            )
        )
        binding.hourlyForecastRecyclerView.adapter = hourlyForecastAdapter

        val locationObserver = Observer<Location> { savedLocation ->
            when (savedLocation) {
                is Location.City -> {
                    binding.introText.visibility = TextView.VISIBLE
                    binding.currentWeatherProgressBar.visibility = ProgressBar.VISIBLE
                    forecastRepository.loadOneCallData(savedLocation.name)
                }
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, locationObserver)

        val forecastObserver = Observer<OneCallForecast> { oneCallForecast ->

            binding.introText.visibility = TextView.GONE
            binding.currentWeatherProgressBar.visibility = ProgressBar.GONE

            binding.hourlyForecastCardView.visibility = CardView.VISIBLE
            binding.sunProgressCardView.visibility = CardView.VISIBLE
            binding.detailsCardView.visibility = CardView.VISIBLE

            binding.locationTextView.text = oneCallForecast.name
            binding.timeTextView.text = formatTime(oneCallForecast.current.date, oneCallForecast.timezone)
            binding.dateTextView.text = formatDate(oneCallForecast.current.date, oneCallForecast.timezone)
            binding.tempTextView.text = formatTempDisplay(oneCallForecast.current.temp, settingsManager.getTempDisplayUnit())
            binding.mainTextView.text = oneCallForecast.current.weather[0].main
            binding.iconImageView.load(iconUrl(oneCallForecast.current.weather[0].icon))

            hourlyForecastAdapter.currentTime = oneCallForecast.current.date
            hourlyForecastAdapter.timeZone = oneCallForecast.timezone
            hourlyForecastAdapter.submitList(oneCallForecast.hourly.subList(0, 24))

            binding.sunriseTextView.text =
                formatTime(oneCallForecast.current.sunrise, oneCallForecast.timezone)
            binding.sunProgressBar.progress =
                getSunProgress(oneCallForecast.current.date, oneCallForecast.current.sunrise, oneCallForecast.current.sunset, oneCallForecast.timezone)
            binding.sunsetTextView.text =
                formatTime(oneCallForecast.current.sunset, oneCallForecast.timezone)

            binding.humidityTextView.text = String.format("%1$1d", oneCallForecast.current.humidity)
            binding.windTextView.text = String.format("%1$.1f km/h", oneCallForecast.current.wind_speed)
            binding.pressureTextView.text = String.format("%1$1d hPa", oneCallForecast.current.pressure)
            binding.visibilityTextView.text = String.format("%1$1d.0 km", oneCallForecast.current.visibility/1000)
        }
        forecastRepository.oneCallForecast.observe(viewLifecycleOwner, forecastObserver)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}