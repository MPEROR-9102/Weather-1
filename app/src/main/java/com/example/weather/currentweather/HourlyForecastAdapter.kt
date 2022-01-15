package com.example.weather.currentweather

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.weather.*
import com.example.weather.api.HourlyForecast
import kotlin.properties.Delegates


class ViewHolder(
    view: View,
    private val currentTime: Long,
    private val timeZone: String,
    private val settingsManager: SettingsManager
) : RecyclerView.ViewHolder(view) {

    private val hourTextView: TextView = view.findViewById(R.id.hourTextView)
    private val iconImageView: ImageView = view.findViewById(R.id.iconImageView)
    private val tempTextView: TextView = view.findViewById(R.id.tempTextView)

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(hourlyForecast: HourlyForecast) {
        hourTextView.text = formatHourlyTime(currentTime, hourlyForecast.date, timeZone)
        iconImageView.load(iconUrl(hourlyForecast.weather[0].icon))
        tempTextView.text =
            formatTempDisplay(hourlyForecast.temp, settingsManager.getTempDisplayUnit())
    }
}

class HourlyForecastAdapter(
    private val settingsManager: SettingsManager
) : ListAdapter<HourlyForecast, ViewHolder>(DIFF_CONFIG) {

    var currentTime by Delegates.notNull<Long>()
    lateinit var timeZone: String

    companion object {
        val DIFF_CONFIG = object : DiffUtil.ItemCallback<HourlyForecast>() {
            override fun areItemsTheSame(
                oldItem: HourlyForecast,
                newItem: HourlyForecast
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: HourlyForecast,
                newItem: HourlyForecast
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.hourly_view_layout, parent, false)
        return ViewHolder(itemView, currentTime, timeZone, settingsManager)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}