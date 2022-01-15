package com.example.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.weather.currentweather.CurrentWeatherFragmentDirections
import com.example.weather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingsManager: SettingsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        val view = binding.root
        setContentView(view)

        binding.toolbar.setupWithNavController(findNavController(R.id.navHostFragment))
        binding.navBar.setupWithNavController(findNavController(R.id.navHostFragment))

        settingsManager = SettingsManager(this)

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            return@setOnMenuItemClickListener when (menuItem.itemId) {
                R.id.toolbarTempItem -> {
                    showTempDisplayDialog()
                    true
                }
                else -> false
            }
        }
        binding.currentLocationButton.setOnClickListener {
            val action =
                CurrentWeatherFragmentDirections.actionCurrentWeatherFragmentToLocationEntryFragment()
            findNavController(R.id.navHostFragment).navigate(action)
        }
    }

    private fun showTempDisplayDialog() {
        val toast = Toast.makeText(this, "Unit Updated, Tap Today to Refresh", Toast.LENGTH_LONG)
        val builder = AlertDialog.Builder(this)
            .setTitle("Choose Temperature Unit")
            .setMessage("Units to display the current temperature.")
            .setPositiveButton("°F") { _, _ ->
                settingsManager.upDateTempDisplayUnit(TempDisplayUnit.Fahrenheit)
                toast.show()
            }
            .setNeutralButton("°C") { _, _ ->
                settingsManager.upDateTempDisplayUnit(TempDisplayUnit.Celsius)
                toast.show()
            }
        builder.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}