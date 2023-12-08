package com.example.travelcompanion.apis

import com.example.travelcompanion.WeatherService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIService{
    @GET("/v2.0/current")
    suspend fun getWeather(@Query("lat") lat: Double = 0.0,
                           @Query("lon") lon: Double = 0.0,
                           @Query("key") key: String = "b7f245921cb448e3aef8c04a103ed0a8") : WeatherService
}

object WeatherAPI{
    private const val base_url = "https://api.weatherbit.io"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json{ ignoreUnknownKeys = true}.asConverterFactory("application/json".toMediaType()))
        .baseUrl(base_url)
        .build()

    val retrofitService: WeatherAPIService by lazy {
        retrofit.create(WeatherAPIService::class.java)
    }
}