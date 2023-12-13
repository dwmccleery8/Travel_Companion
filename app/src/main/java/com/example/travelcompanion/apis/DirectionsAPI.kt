package com.example.travelcompanion.apis

import com.example.travelcompanion.models.DirectionsObjects
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface DirectionsAPIService {
    @GET("/maps/api/directions/json")
    suspend fun getDirections(
        @Query("destination") destination: String = "",
        @Query("origin") origin: String = (""),
        @Query("departure_time") departure_time: Int = 1343641500,
        @Query("key") key: String = "AIzaSyB7ss5UlmKGPVl0XnppULoJUFHfajZe9Fc"
    ): DirectionsObjects
}

object DirectionsAPI {
    private const val base_url = "https://maps.googleapis.com"


    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json {
            ignoreUnknownKeys = true
        }.asConverterFactory("application/json".toMediaType()))
        .baseUrl(base_url)
        .client(
            OkHttpClient.Builder()
            .addInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                    val url = request.url
                    println("Request URL: $url")
                    return chain.proceed(request)
                }
            })
            .build())

        .build()

    val retrofitService: DirectionsAPIService by lazy {
        retrofit.create(DirectionsAPIService::class.java)
    }

}