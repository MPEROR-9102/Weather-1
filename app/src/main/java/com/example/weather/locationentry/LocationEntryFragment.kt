package com.example.weather.locationentry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.weather.LocationRepository
import com.example.weather.R
import com.example.weather.databinding.FragmentLocationEntryBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LocationEntryFragment : Fragment() {

    private var _binding: FragmentLocationEntryBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationRepository: LocationRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLocationEntryBinding.inflate(inflater, container, false)

        activity?.findViewById<FloatingActionButton>(R.id.currentLocationButton)?.hide()

        locationRepository = LocationRepository(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.locationSubmitButton.setOnClickListener {
            val cityName = binding.locationEditText.text
            when {
                cityName.isEmpty() -> binding.locationEditText.error = "Enter a City Name"
                else -> {
                    locationRepository.saveLocation(cityName.toString().capitalize())
                    findNavController().navigateUp()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}