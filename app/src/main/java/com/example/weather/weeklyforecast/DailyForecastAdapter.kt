package com.example.weather.weeklyforecast

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
import com.example.weather.R
import com.example.weather.api.DailyForecast
import com.example.weather.formatDate
import com.example.weather.iconUrl

class ViewHolder(
    view: View,
    private val timeZone: String
) : RecyclerView.ViewHolder(view) {
    private val dateTextView: TextView = view.findViewById(R.id.dateTextView)
    private val iconImageView: ImageView = view.findViewById(R.id.iconImageView)
    private val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(dailyForecast: DailyForecast) {
        dateTextView.text = formatDate(dailyForecast.date, timeZone)
        iconImageView.load(iconUrl(dailyForecast.weather[0].icon))
        descriptionTextView.text = dailyForecast.weather[0].main
    }
}

class DailyForecastAdapter : ListAdapter<DailyForecast, ViewHolder>(DIFF_CONFIG) {

    lateinit var timeZone: String

    companion object {
        val DIFF_CONFIG = object : DiffUtil.ItemCallback<DailyForecast>() {
            override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: DailyForecast,
                newItem: DailyForecast
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.daily_view_layout, parent, false)
        return ViewHolder(itemView, timeZone)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}