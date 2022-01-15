package com.example.weather.weeklyforecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.ForecastRepository
import com.example.weather.Location
import com.example.weather.LocationRepository
import com.example.weather.R
import com.example.weather.api.OneCallForecast
import com.example.weather.databinding.FragmentWeeklyForecastBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class WeeklyForecastFragment : Fragment() {

    private var _biding: FragmentWeeklyForecastBinding? = null
    private val binding get() = _biding!!

    private lateinit var locationRepository: LocationRepository
    private lateinit var forecastRepository: ForecastRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _biding = FragmentWeeklyForecastBinding.inflate(inflater, container, false)

        activity?.findViewById<FloatingActionButton>(R.id.currentLocationButton)?.hide()

        locationRepository = LocationRepository(requireContext())
        forecastRepository = ForecastRepository()

        binding.dailyForecastRecyclerView.layoutManager = GridLayoutManager(
            requireContext(), 2,
            GridLayoutManager.VERTICAL, false
        )
        val dailyForecastAdapter = DailyForecastAdapter()
        binding.dailyForecastRecyclerView.adapter = dailyForecastAdapter

        val locationObserver = Observer<Location> { location ->
            when (location) {
                is Location.City -> {
                    binding.introText.visibility = TextView.VISIBLE
                    binding.currentWeatherProgressBar.visibility = ProgressBar.VISIBLE
                    forecastRepository.loadOneCallData(location.name)
                }
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, locationObserver)

        val forecastObserver = Observer<OneCallForecast> { oneCallForecast ->
            binding.introText.visibility = TextView.GONE
            binding.currentWeatherProgressBar.visibility = ProgressBar.GONE
            binding.dailyForecastRecyclerView.visibility = RecyclerView.VISIBLE

            dailyForecastAdapter.timeZone = oneCallForecast.timezone
            dailyForecastAdapter.submitList(oneCallForecast.daily)
        }
        forecastRepository.oneCallForecast.observe(viewLifecycleOwner, forecastObserver)

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _biding = null
    }
}