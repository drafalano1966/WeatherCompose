package com.example.weathercompose.ConversionExt

const val KELVIN_CONVERSION_FACTOR = 273.15
const val KILOMETER_CONVERSION_FACTOR = 1000

fun Double.toCelsius() = this - KELVIN_CONVERSION_FACTOR

fun Double.toFahrenheit() = (this - KELVIN_CONVERSION_FACTOR) * 9 / 5 + 32

fun Int.toKilometer(): String =
    if (this > KILOMETER_CONVERSION_FACTOR) "${(this / KILOMETER_CONVERSION_FACTOR).toInt()} km"
    else "$this m"