package com.example.travelcompanion.apis

import com.example.travelcompanion.models.ChatRequest
import com.example.travelcompanion.models.ChatResponse
import com.example.travelcompanion.models.Message
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface OpenAiApiService {

    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer api_key"
    )
    @POST("v1/chat/completions")
    suspend fun getChatCompletions(@Body request: ChatRequest) : ChatResponse

}

object OpenAiApi{

    private val httpClient = OkHttpClient.Builder()
        .callTimeout(50, TimeUnit.SECONDS)
        .readTimeout(50, TimeUnit.SECONDS).build()

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json{ ignoreUnknownKeys = true}.asConverterFactory("application/json".toMediaType()))
        .baseUrl("https://api.openai.com/")
        .client(httpClient)
        .build()

    val retrofitService : OpenAiApiService by lazy{
        retrofit.create(OpenAiApiService::class.java)
    }

    suspend fun getResponse(prompt: String) : String{
        val chatRequest = ChatRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(
                Message(role = "user",
                    content = prompt)
            )
        )
        val chatResponse = retrofitService.getChatCompletions(chatRequest)
        return chatResponse.choices[0].message.content
    }

}