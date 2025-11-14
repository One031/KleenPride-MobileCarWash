package com.example.kleenpride.data.models

/**
 * Data class representing a car wash service
 */
data class Service(
    val id: Int,
    val name: String,
    val duration: String,
    val price: String,
    val description: String = ""
)

/**
 * Data class representing vehicle type
 */
data class CarType(
    val id: Int,
    val name: String,
    val icon: String = "🚗"
)