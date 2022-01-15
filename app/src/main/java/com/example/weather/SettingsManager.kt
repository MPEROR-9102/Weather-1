package com.example.weather

import android.content.Context

enum class TempDisplayUnit {
    Fahrenheit, Celsius
}

private const val KEY_UNIT = "key_unit"

class SettingsManager(context: Context) {

    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun upDateTempDisplayUnit(tempDisplayUnit: TempDisplayUnit) {
        when (tempDisplayUnit) {
            TempDisplayUnit.Fahrenheit -> preferences.edit()
                .putString(KEY_UNIT, tempDisplayUnit.name).apply()
            TempDisplayUnit.Celsius -> preferences.edit().putString(KEY_UNIT, tempDisplayUnit.name)
                .apply()
        }
    }

    fun getTempDisplayUnit(): TempDisplayUnit {
        val tempUnit = preferences.getString(KEY_UNIT, TempDisplayUnit.Fahrenheit.name)
            ?: TempDisplayUnit.Fahrenheit.name
        return TempDisplayUnit.valueOf(tempUnit)
    }
}