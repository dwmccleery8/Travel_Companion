package com.example.travelcompanion.apis

import com.example.travelcompanion.models.AddressResult
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingAPIService {
    @GET("/v1/geocode/autocomplete")
    suspend fun getAddress(
        @Query("text") text: String,
        @Query("format") format: String = "json",
        @Query("apiKey") apiKey: String = "848cf8aabdef48b5839af615c01390fe"
    ): AddressResult
}


object GeocodingAPI {
    private const val base_url = "https://api.geoapify.com"


    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json {
            ignoreUnknownKeys = true
        }.asConverterFactory("application/json".toMediaType()))
        .baseUrl(base_url)
        .build()

    val retrofitService: GeocodingAPIService by lazy {
        retrofit.create(GeocodingAPIService::class.java)
    }

}