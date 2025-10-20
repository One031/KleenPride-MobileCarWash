package com.example.kleenpride.ui.booking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.kleenpride.viewmodel.BookingViewModel

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
            BookingScreen(viewModel = viewModel)
        }
    }
}