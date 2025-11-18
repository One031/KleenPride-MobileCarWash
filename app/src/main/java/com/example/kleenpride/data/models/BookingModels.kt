package com.example.kleenpride.data.models

import com.google.firebase.firestore.DocumentId
import java.util.*

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

/**
 * Data class representing a payment method
 */

data class PaymentMethod(
    val id: String,
    val name: String,
    val lastDigits: String,
    val icon: String = "💳"
)

/**
 * Booking state holder - centralizes all booking data
 */

data class BookingState(
    val selectedService: Service? = null,
    val selectedDate: Date? = null,
    val selectedTime: String? = null,
    val address: String = "",
    val selectedCarType: CarType? = null,
    val selectedPaymentMethod: PaymentMethod? = null
    ) {
    fun isValid(): Boolean {
        return selectedService != null &&
                selectedDate != null &&
                selectedTime != null &&
                address.isNotBlank() &&
                selectedCarType != null &&
                selectedPaymentMethod != null
    }
}