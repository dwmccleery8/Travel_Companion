package com.example.travelcompanion

import kotlinx.serialization.Serializable

@Serializable
data class WeatherServiceDaily(
    val data: List<WeatherDetailsDaily>,
    val city_name: String,
    val lon: Double,
    val timezone: String,
    val lat: Double,
    val country_code: String,
    val state_code: String
)
@Serializable
data class WeatherDetailsDaily(
    val valid_date: String,
    val ts: Long,
    val datetime: String,
    val wind_gust_spd: Double,
    val wind_spd: Double,
    val wind_dir: Int,
    val wind_cdir: String,
    val wind_cdir_full: String,
    val temp: Double,
    val max_temp: Double,
    val min_temp: Double,
    val high_temp: Double,
    val low_temp: Double,
    val app_max_temp: Double,
    val app_min_temp: Double,
    val pop: Int,
    val precip: Double,
    val snow: Double,
    val snow_depth: Double,
    val slp: Double,
    val pres: Double,
    val weather: WeatherInfoDaily,
)
@Serializable
data class WeatherInfoDaily(
    val icon: String,
    val code: Int,
    val description: String
)
