package com.example.kleenpride.ui.booking.createbooking

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.kleenpride.ui.booking.BookingActivity

class CreateBookingActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CreateNewBookingScreen(
                onBookingConfirmed = { state ->

                    // SEND BOOKING DETAILS TO BOOKING ACTIVITY
                    val intent = Intent(this, BookingActivity::class.java).apply {
                        putExtra("serviceName", state.selectedService?.name)
                        putExtra("servicePrice", state.selectedService?.price)
                        putExtra("serviceDuration", state.selectedService?.duration)
                        putExtra("carType", state.selectedCarType?.name)
                        putExtra("date", state.selectedDate?.time ?: 0L)
                        putExtra("time", state.selectedTime)
                        putExtra("address", state.address)
                        putExtra("paymentMethod", state.selectedPaymentMethod?.name)
                    }

                    startActivity(intent)
                    finish()
                }
            )
        }
    }
}
