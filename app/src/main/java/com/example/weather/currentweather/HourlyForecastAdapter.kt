package com.example.weather.currentweather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weather.*
import com.example.weather.api.HourlyForecast
import java.util.*


class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val hourTextView: TextView = view.findViewById(R.id.hourTextView)
    private val iconImageView: ImageView = view.findViewById(R.id.iconImageView)
    private val tempTextView: TextView = view.findViewById(R.id.tempTextView)

    fun bind(hourlyForecast: HourlyForecast) {
        hourTextView.text = formatTime(hourlyForecast.date, TimeFormatType.Hour)
        iconImageView.load(iconUrl(hourlyForecast.weather[0].icon))
        tempTextView.text = formatTempDisplay(hourlyForecast.temp)
    }
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
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.hourly_view_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}