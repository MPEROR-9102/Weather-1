package com.example.weather

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

enum class TimeFormatType {
    FullTime, Hour
}

@SuppressLint("SimpleDateFormat")
private val DATE_FORMAT = SimpleDateFormat("MM-dd-yy")
@SuppressLint("SimpleDateFormat")
private val TIME_FORMAT_HOUR = SimpleDateFormat("HH")
@SuppressLint("SimpleDateFormat")
private val TIME_FORMAT_MIN = SimpleDateFormat("mm")
@SuppressLint("SimpleDateFormat")
private val TIME_12_CLOCK = SimpleDateFormat("a")


fun iconUrl(iconId: String) = "http://openweathermap.org/img/wn/$iconId@2x.png"

fun formatTempDisplay(temp: Float) = String.format("%.0f°", temp)

fun formatDate(dt: Long) = DATE_FORMAT.format(Date(dt*1000))

private fun getCalenderHour(hourType: Int = Calendar.HOUR) : Int {
    val calendar = Calendar.getInstance()
    calendar.time = Date()
    return when(hourType) {
        Calendar.HOUR -> calendar.get(Calendar.HOUR)
        Calendar.HOUR_OF_DAY -> calendar.get(Calendar.HOUR_OF_DAY)
        Calendar.MINUTE -> calendar.get(Calendar.MINUTE)
        else -> -1
    }
}

fun formatTime(dt: Long, formatType: TimeFormatType) : String {
    var hour = TIME_FORMAT_HOUR.format(Date(dt*1000)).toInt()
    if (hour > 12)
        hour -= 12
    val clock = TIME_12_CLOCK.format(Date(dt*1000)).uppercase()

    return when (formatType) {
        TimeFormatType.Hour ->  {
            if (hour == getCalenderHour()) {
                "Now"
            } else
                String.format("$hour $clock")
        }
        TimeFormatType.FullTime -> {
            val min = TIME_FORMAT_MIN.format(Date(dt*1000))
            String.format("$hour:$min $clock")
        }
    }
}

private fun hourInMin(dt: Long) =
    TIME_FORMAT_HOUR.format(Date(dt * 1000)).toInt() * 60 + TIME_FORMAT_MIN.format(Date(dt * 1000)).toInt()

fun getSunProgress(rise: Long, set: Long) : Int {
    val riseMin = hourInMin(rise)
    val setMin = hourInMin(set)
    val currentMin = getCalenderHour(Calendar.HOUR_OF_DAY) * 60 + getCalenderHour(Calendar.MINUTE)

    val estimatedInterval = setMin - riseMin
    val liveStat = currentMin - riseMin

    return when {
        liveStat <= riseMin -> 0
        (liveStat in (riseMin + 1)..setMin) -> (liveStat*100)/estimatedInterval
        else -> 100
    }
}
