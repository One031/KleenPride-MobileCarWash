package com.example.kleenpride.ui.booking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.*
import com.example.kleenpride.data.booking.Booking
import com.example.kleenpride.data.models.BookingState
import com.example.kleenpride.ui.homescreen.ServiceItem
import com.example.kleenpride.viewmodel.BookingViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Date
import com.example.kleenpride.R


class BookingActivity : ComponentActivity() {

    private val viewModel: BookingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1 — If Booking sent from Home or another Activity, save it
        intent?.let { intent ->
            val serviceName = intent.getStringExtra("serviceName")
            if (serviceName != null) {
                val booking = Booking(
                    id = "",
                    userId = FirebaseAuth.getInstance().currentUser?.uid ?: "",
                    serviceName = serviceName,
                    servicePrice = intent.getStringExtra("servicePrice") ?: "",
                    serviceDuration = intent.getStringExtra("serviceDuration") ?: "",
                    carType = intent.getStringExtra("carType") ?: "",
                    date = Date(intent.getLongExtra("date", 0L)),
                    time = intent.getStringExtra("time") ?: "",
                    address = intent.getStringExtra("address") ?: "",
                    paymentMethod = intent.getStringExtra("paymentMethod") ?: "",
                    status = "Active",
                    createdAt = Date(),
                    updatedAt = Date()
                )
                viewModel.createBooking(booking)
            }
        }

        // STEP 2 — Load all existing bookings
        viewModel.loadBookings()

        // STEP 3 — Prepare UI
        setContent {
            var currentScreen by remember { mutableStateOf("list") }

            // Prefill ServiceItem if intent has data
            remember {
                intent?.getStringExtra("serviceName")?.let { name ->
                    val price = intent.getStringExtra("servicePrice") ?: ""
                    val duration = intent.getStringExtra("serviceDuration") ?: ""
                    val description = intent.getStringExtra("serviceDescription") ?: ""

                    // Convert included string to list
                    val included = intent.getStringExtra("serviceIncluded")?.split(",")?.map { it.trim() } ?: emptyList()

                    // Get image resource ID (pass as Int in intent)
                    val imageResId = intent.getIntExtra("serviceImage", R.drawable.pridewash)

                    ServiceItem(
                        name = name,
                        image = imageResId,
                        price = price,
                        duration = duration,
                        description = description,
                        included = included
                    )
                }
            }


            when (currentScreen) {
                "list" -> BookingScreen(
                    viewModel = viewModel,
                    onCreateBooking = { currentScreen = "create" }
                )

                "create" -> CreateNewBookingScreen(
                    onBookingConfirmed = { bookingState ->
                        val booking = bookingStateToBooking(bookingState)
                        viewModel.createBooking(booking)
                        currentScreen = "list"
                    }
                )
            }
        }
    }

    // Helper: convert BookingState -> Booking
    private fun bookingStateToBooking(state: BookingState): Booking {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        return Booking(
            id = "",
            userId = currentUserId,
            serviceName = state.selectedService?.name ?: "",
            servicePrice = state.selectedService?.price ?: "",
            serviceDuration = state.selectedService?.duration ?: "",
            carType = state.selectedCarType?.name ?: "",
            date = state.selectedDate ?: Date(),
            time = state.selectedTime ?: "",
            address = state.address,
            paymentMethod = state.selectedPaymentMethod?.name ?: "",
            status = "Active",
            createdAt = Date(),
            updatedAt = Date()
        )
    }
}
