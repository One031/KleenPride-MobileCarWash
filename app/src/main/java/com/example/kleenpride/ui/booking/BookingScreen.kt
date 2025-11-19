package com.example.kleenpride.ui.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kleenpride.data.booking.Booking
import com.example.kleenpride.ui.components.BottomNavBar
import com.example.kleenpride.ui.theme.LimeGreen
import com.example.kleenpride.viewmodel.BookingViewModel

/**
 * BookingScreen - Displays user's active and recent bookings
 */
@Composable
fun BookingScreen(
    viewModel: BookingViewModel = viewModel(),
    onCreateBooking: () -> Unit = {}
) {
    // Observe bookings list from the ViewModel
    val bookings by viewModel.bookings.observeAsState(null) // null means loading
    val error by viewModel.error.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Add status bar padding and additional spacing
        Spacer(modifier = Modifier.height(48.dp))

        // Check if still loading
        when {
            bookings == null -> {
                // Loading state
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = LimeGreen,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Loading bookings...",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            bookings!!.isEmpty() -> {
                // Empty state
                EmptyBookingScreen(onCreateBookingClick = onCreateBooking)
            }

            else -> {
                // Has bookings - show normal view
                BookingContent(
                    bookings = bookings!!,
                    onCreateBooking = onCreateBooking
                )
            }
        }
    }
}

@Composable
private fun BookingContent(
    bookings: List<Booking>,
    onCreateBooking: () -> Unit
) {
    // Find the active booking if available
    val activeBooking = bookings.firstOrNull { it.status == "Active" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Page Title
        Text(
            text = "Bookings",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Active Booking tile
        if (activeBooking != null) {
            ActiveBookingCard(activeBooking)
        } else {
            NoActiveBookingCard()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Booking Button
        Button(
            onClick = onCreateBooking,
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 7.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LimeGreen
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Create New Booking",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recent Bookings Section
        Text(
            text = "Recent Bookings",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Lazy Column to display recent bookings
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Take remaining space
        ) {
            items(bookings.take(3)) { booking ->
                BookingItem(booking)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom Navigation Bar
        BottomNavBar(currentScreen = "booking")
    }
}