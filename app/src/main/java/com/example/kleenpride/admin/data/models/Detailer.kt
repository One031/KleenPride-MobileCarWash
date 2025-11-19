package com.example.kleenpride.admin.data.models

import androidx.compose.ui.graphics.Color
import com.example.kleenpride.ui.theme.LimeGreen
import java.text.SimpleDateFormat
import java.util.*

data class Detailer(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val rating: Float = 0f,
    val totalJobs: Int = 0,
    val earnings: Int = 0,
    val status: String = "ACTIVE",
    val joinDate: String = "", // Keep as String for display
    val role: String = "DETAILER"
) {
    // UI-specific properties as computed properties
    val displayName: String get() = "$firstName $lastName"

    val statusColor: Color get() = when (status) {
        "ACTIVE" -> LimeGreen
        "INACTIVE" -> Color.Red
        "DELETED" -> Color.Gray
        else -> LimeGreen
    }

    val formattedEarnings: String get() = "R${String.format("%,d", earnings)}"

    val formattedRating: String get() = "%.1f".format(rating)

    val initials: String get() =
        (firstName.firstOrNull()?.toString() ?: "") +
                (lastName.firstOrNull()?.toString() ?: "")

    // Helper to get a default join date if empty
    val displayJoinDate: String get() =
        if (joinDate.isNotEmpty()) joinDate
        else SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(Date())
}