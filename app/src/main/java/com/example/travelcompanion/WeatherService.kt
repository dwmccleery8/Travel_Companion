package com.example.travelcompanion

import kotlinx.serialization.Serializable


@Serializable
data class WeatherService(
    val data: List<WeatherDetails>,
    val city_name: String,
    val lon: Double,
    val timezone: String,
    val lat: Double,
    val country_code: String,
    val state_code: String
)

@Serializable
data class WeatherDetails(
    val timestamp_local: String,
    val timestamp_utc: String,
    val ts: Long,
    val datetime: String,
    val wind_gust_spd: Double,
    val wind_spd: Double,
    val wind_dir: Int,
    val wind_cdir: String,
    val wind_cdir_full: String,
    val temp: Double,
    val app_temp: Double,
    val pop: Int,
    val precip: Double,
    val snow: Double,
    val snow_depth: Double,
    val slp: Double,
    val pres: Double,
    val dewpt: Double,
    val rh: Double,
    val weather: WeatherInfo,
    val pod: String,
    val clouds_low: Int,
    val clouds_mid: Int,
    val clouds_hi: Int,
    val clouds: Int,
    val vis: Double,
    val dhi: Double,
    val dni: Double,
    val ghi: Double,
    val solar_rad: Double,
    val uv: Double,
    val ozone: Double
)

@Serializable
data class WeatherInfo(
    val icon: String,
    val description: String,
    val code: Int
)
