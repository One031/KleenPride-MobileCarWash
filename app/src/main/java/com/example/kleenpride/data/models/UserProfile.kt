package com.example.kleenpride.data.models

data class UserProfile(
    val uid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val carSize: String = "",
    val receiveReminders: Boolean = true,
    val receivePromotions: Boolean = true,
    val enableNotifications: Boolean = true,
    val preferredName: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val favourites: List<String> = emptyList()  // <- add this
)