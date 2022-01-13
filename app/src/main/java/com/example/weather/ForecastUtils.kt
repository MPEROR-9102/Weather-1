package com.example.weather

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
fun getClock(dt: Long, timeZone: String): ZonedDateTime = Instant.ofEpochSecond(dt).atZone(ZoneId.of(timeZone))

fun iconUrl(iconId: String) = "http://openweathermap.org/img/wn/$iconId@2x.png"

fun formatTempDisplay(temp: Float, tempDisplayUnit: TempDisplayUnit) : String {
    return when (tempDisplayUnit) {
        TempDisplayUnit.Fahrenheit -> String.format("%.0f°", temp)
        TempDisplayUnit.Celsius -> {
            val celsiusTemp = (temp - 32f) * (5f / 9f)
            String.format("%.0f°", celsiusTemp)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dt: Long, timeZone: String) = getClock(dt, timeZone).toLocalDate().toString()

@RequiresApi(Build.VERSION_CODES.O)
fun formatTime(dt: Long, timeZone: String): String {
    var hour = getClock(dt, timeZone).hour
    var clock = "AM"
    if (hour > 12) {
        hour -= 12
        clock = "PM"
    }
    val min = getClock(dt, timeZone).minute
    return String.format("$hour:$min $clock")
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
