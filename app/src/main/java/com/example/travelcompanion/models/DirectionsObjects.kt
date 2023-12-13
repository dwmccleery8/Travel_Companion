package com.example.travelcompanion.models

import kotlinx.serialization.Serializable

@Serializable
data class DirectionsObjects(
    val routes: List<DirectionsRoute>
)

@Serializable
data class DirectionsRoute(
    val bounds: Bounds,
    val copyrights: String,
    val legs: List<DirectionsLeg>,
    val overview_polyline: DirectionsPolyline,
    val summary: String,
    val warnings: List<String>,
    val waypoint_order: List<Int>
)
@Serializable
data class Bounds(
    val northeast: LatLngLiteral,
    val southwest: LatLngLiteral
)
@Serializable
data class LatLngLiteral(
    val lat: Double,
    val lng: Double
)
@Serializable
data class DirectionsLeg(
    val end_address: String,
    val end_location: LatLngLiteral,
    val start_address: String,
    val start_location: LatLngLiteral,
    val steps: List<DirectionsStep>,
    val duration: TextValueObject
)
@Serializable
data class DirectionsStep(
    val duration: TextValueObject,
    val end_location: LatLngLiteral,
    val html_instructions: String,
    val polyline: DirectionsPolyline,
    val start_location: LatLngLiteral
)
@Serializable
data class TextValueObject(
    val text: String,
    val value: Int
)
@Serializable
data class DirectionsPolyline(
    val points: String
)