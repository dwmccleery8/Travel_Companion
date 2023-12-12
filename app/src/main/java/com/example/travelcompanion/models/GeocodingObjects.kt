package com.example.travelcompanion.models

import kotlinx.serialization.Serializable

@Serializable
data class AddressResult(
    val results: List<ResultAddress>,
)

@Serializable
data class ResultAddress(
    val lon: Double,
    val lat: Double,
    val formatted: String
)