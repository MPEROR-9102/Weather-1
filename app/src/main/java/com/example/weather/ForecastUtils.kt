package com.example.weather

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

private val DIRECTIONS = listOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")

@RequiresApi(Build.VERSION_CODES.O)
private fun getClock(dt: Long, timeZone: String): ZonedDateTime =
    Instant.ofEpochSecond(dt).atZone(ZoneId.of(timeZone))

fun iconUrl(iconId: String) = "http://openweathermap.org/img/wn/$iconId@2x.png"

fun formatTempDisplay(temp: Float, tempDisplayUnit: TempDisplayUnit): String {
    return when (tempDisplayUnit) {
        TempDisplayUnit.Fahrenheit -> String.format("%.0f°", temp)
        TempDisplayUnit.Celsius -> {
            val celsiusTemp = (temp - 32f) * (5f / 9f)
            String.format("%.0f°", celsiusTemp)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dt: Long, timeZone: String): String {
    val clock = getClock(dt, timeZone)
    return "${clock.dayOfWeek.name.lowercase().capitalize().substring(0, 3)}, " +
            clock.month.name.lowercase().capitalize().substring(0, 3) + " " +
            clock.toLocalDate().toString().substring(8)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(dt: Long, timeZone: String): String {
    var hour = getClock(dt, timeZone).hour
    var clock = "AM"

    if (hour >= 12)
        clock = "PM"
    if (hour > 12)
        hour -= 12
    val min = getClock(dt, timeZone).minute
    return when (min.toString().length) {
        1 -> String.format("$hour:0$min $clock")
        2 -> String.format("$hour:$min $clock")
        else -> ""
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun getHour(dt: Long, timeZone: String): String {
    var hour = getClock(dt, timeZone).hour
    var clock = "AM"

    if (hour >= 12)
        clock = "PM"
    if (hour > 12)
        hour -= 12
    return "$hour $clock"
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatHourlyTime(currentTime: Long, hourlyTime: Long, timeZone: String): String {
    val current = getHour(currentTime, timeZone)
    val hourly = getHour(hourlyTime, timeZone)
    return if (current == hourly) "Now" else hourly
}

@RequiresApi(Build.VERSION_CODES.O)
private fun hourInMin(dt: Long, timeZone: String) =
    getClock(dt, timeZone).hour * 60 + getClock(dt, timeZone).minute

@RequiresApi(Build.VERSION_CODES.O)
fun getSunProgress(dt: Long, rise: Long, set: Long, timeZone: String): Int {
    val riseMin = hourInMin(rise, timeZone)
    val setMin = hourInMin(set, timeZone)
    val currentMin = getClock(dt, timeZone).hour * 60 + getClock(dt, timeZone).minute

    val estimatedInterval = setMin - riseMin
    val liveStat = currentMin - riseMin

    return when {
        currentMin <= riseMin -> 0
        (currentMin in (riseMin + 1)..setMin) -> (liveStat * 100) / estimatedInterval
        else -> 100
    }
}

fun getDirection(degrees: Int) = DIRECTIONS[degrees / 45]
