package com.example.weather.currentweather

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.api.HourlyForecast

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

}

class HourlyForecastAdapter : ListAdapter<HourlyForecast, ViewHolder>(DIFF_CONFIG) {

    companion object {
        val DIFF_CONFIG = object : DiffUtil.ItemCallback<HourlyForecast>() {
            override fun areItemsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: HourlyForecast, newItem: HourlyForecast): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}