package com.example.travelcompanion

import kotlinx.serialization.Serializable


@Serializable
data class WeatherService(
    val data: List<WeatherDetails>,
    // val minutely: List<WeatherDetails>,
    val count: Int
)
@Serializable
data class WeatherDetails(
    val wind_cdir: String,
    val rh: Double,
    val pod: String,
    val lon: Double,
    val pres: Double,
    val timezone: String,
    val ob_time: String,
    val country_code: String,
    val clouds: Double,
    val vis: Double,
    val wind_spd: Double,
    val gust: Double,
    val wind_cdir_full: String,
    val app_temp: Double,
    val state_code: String,
    val ts: Long,
    val h_angle: Int? = null,
    val dewpt: Double,
    val weather: WeatherInfo,
    val uv: Double,
    val aqi: Int,
    val station: String,
    val sources: List<String>,
    val wind_dir: Int,
    val elev_angle: Double,
    val datetime: String,
    val precip: Double,
    val ghi: Double,
    val dni: Double,
    val dhi: Double,
    val solar_rad: Double,
    val city_name: String,
    val sunrise: String,
    val sunset: String,
    val temp: Double,
    val lat: Double,
    val slp: Double
)

@Serializable
data class WeatherInfo(
    val icon: String,
    val description: String,
    val code: Int
)
