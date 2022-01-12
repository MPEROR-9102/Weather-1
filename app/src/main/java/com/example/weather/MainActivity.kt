package com.example.weather

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weather.currentweather.CurrentWeatherFragmentDirections
import com.example.weather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        val view = binding.root
        setContentView(view)

        binding.navBar.setupWithNavController(findNavController(R.id.navHostFragment))

        binding.currentLocationButton.setOnClickListener {
            val action = CurrentWeatherFragmentDirections.actionCurrentWeatherFragmentToLocationEntryFragment()
            findNavController(R.id.navHostFragment).navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}