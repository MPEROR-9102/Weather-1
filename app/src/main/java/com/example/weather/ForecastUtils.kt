package com.example.weather

import java.text.SimpleDateFormat
import java.util.*

enum class FormatType {
    NormHour, SunHour, Date
}

private val DATE_FORMAT = SimpleDateFormat("MM-dd-yy")
private val TIME_FORMAT_NORM = SimpleDateFormat("HH a")
private val TIME_FORMAT_SUN = SimpleDateFormat("HH:mm a")

fun iconUrl(iconId: String) = "http://openweathermap.org/img/wn/$iconId@2x.png"

fun formatTempDisplay(temp: Float) = String.format("%.0f°", temp)

fun formatDateTime(dt: Long, type: FormatType) : String {
    return when(type) {
        FormatType.Date -> DATE_FORMAT.format(Date(dt*1000))
        FormatType.NormHour -> TIME_FORMAT_NORM.format(Date(dt*1000))
        FormatType.SunHour -> TIME_FORMAT_SUN.format(Date(dt*1000))
    }
}