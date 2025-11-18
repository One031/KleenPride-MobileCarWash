package com.example.kleenpride.ui.booking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.example.kleenpride.data.booking.Booking
import com.example.kleenpride.data.models.BookingState
import com.example.kleenpride.viewmodel.BookingViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Date


/**
 * Activity that serves as the entry point for the Booking feature
 * It creates the BookingViewModel and provides it to the Composable UI
 */

class BookingActivity : ComponentActivity() {

    // Create and retain the ViewModel instance using the viewModels delegate
    private val viewModel: BookingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Fetch all bookings when this screen starts
        viewModel.loadBookings()

        // Set the UI content using the BookingScreen Composable
        setContent {
            // State to track which screen to show
            var currentScreen by remember { mutableStateOf("list") }

            when (currentScreen) {
                "list" -> BookingScreen(
                    viewModel = viewModel,
                    onCreateBooking = { currentScreen = "create" }
                )

                "create" -> CreateNewBookingScreen(
                    onBookingConfirmed = { bookingState ->
                        // Convert BookingState to Booking and save
                        val booking = bookingStateToBooking(bookingState)
                        viewModel.createBooking(booking)

                        // Navigate back to list
                        currentScreen = "list"
                    }
                )
            }
        }
    }


    /**
     * Converts BookingState from the form to a Booking entity for the database
     */

    private fun bookingStateToBooking(state: BookingState): Booking {
       // Get current user ID from Firebase Auth

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        return Booking(
            id = "", // Firestore will auto-generate this
            userId = currentUserId,
            serviceName = state.selectedService?.name ?: "",
            servicePrice = state.selectedService?.price ?: "",
            serviceDuration = state.selectedService?.duration ?: "",
            carType = state.selectedCarType?.name ?: "",
            date = state.selectedDate ?: Date(),
            time = state.selectedTime ?: "",
            address = state.address,
            paymentMethod = state.selectedPaymentMethod?.name ?: "",
            status = "Active", // New bookings start as Active
            createdAt = Date(),
            updatedAt = Date()
        )
    }
}