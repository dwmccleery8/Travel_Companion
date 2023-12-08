package com.example.travelcompanion.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(val model: String, val messages: List<Message>, val temperature: Double = 0.8)

@Serializable
data class Message(val role: String, val content: String)

@Serializable
data class ChatResponse(val id: String,
                        @SerialName(value = "object")
                        val object_: String,
                        val created: Long, val model: String, val usage: Usage, val choices: List<Choice>)

@Serializable
data class Usage(val prompt_tokens: Int, val completion_tokens: Int, val total_tokens: Int)

@Serializable
data class Choice(val message: Message, val finish_reason: String, val index: Int)