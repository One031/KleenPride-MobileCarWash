package com.example.kleenpride.ui.booking

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.kleenpride.data.models.BookingState

class CreateBookingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get all preselected data from intent
        val preselectedServiceName = intent.getStringExtra("preselectedServiceName").orEmpty()
        val preselectedServicePrice = intent.getStringExtra("preselectedServicePrice").orEmpty()
        val preselectedServiceDuration = intent.getStringExtra("preselectedServiceDuration").orEmpty()
        val preselectedLocation = intent.getStringExtra("preselectedLocation").orEmpty()
        val preselectedCarType = intent.getStringExtra("preselectedCarType").orEmpty()

        setContent {
            CreateNewBookingScreen(
                preselectedServiceName = preselectedServiceName,
                preselectedServicePrice = preselectedServicePrice,
                preselectedServiceDuration = preselectedServiceDuration,
                preselectedLocation = preselectedLocation,
                preselectedCarType = preselectedCarType,
                onBookingConfirmed = { state: BookingState ->
                    // Create intent for BookingActivity
                    val bookingIntent = Intent(this, BookingActivity::class.java).apply {
                        // Service details
                        putExtra("serviceName", state.selectedService?.name ?: "")
                        putExtra("servicePrice", state.selectedService?.price ?: "")
                        putExtra("serviceDuration", state.selectedService?.duration ?: "")

                        // Other booking details
                        putExtra("carType", state.selectedCarType?.name ?: "")
                        putExtra("date", state.selectedDate?.time ?: 0L)
                        putExtra("time", state.selectedTime ?: "")
                        putExtra("address", state.address ?: "")
                        putExtra("paymentMethod", state.selectedPaymentMethod?.name ?: "")
                    }

                    startActivity(bookingIntent)
                    finish()
                }
            )
        }
    }
}