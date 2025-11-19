package com.example.kleenpride.ui.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
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

    val bookings by viewModel.bookings.observeAsState(emptyList())

    // No bookings? Show empty screen then return
    if (bookings.isEmpty()) {
        EmptyBookingScreen(onCreateBookingClick = onCreateBooking)
        return
    }

    val activeBooking = bookings.firstOrNull { it.status == "Active" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Bookings",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        if (activeBooking != null) {
            ActiveBookingCard(activeBooking)
        } else {
            NoActiveBookingCard()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Create Booking button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 7.dp)
        ) {
            Button(
                onClick = onCreateBooking,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LimeGreen),
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
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Recent Bookings
        Text(
            text = "Recent Bookings",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(bookings.take(3)) { booking ->
                BookingItem(booking)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ⭐ Here's the MAP at the bottom
        MapScreen()

        Spacer(modifier = Modifier.height(24.dp))

        BottomNavBar(currentScreen = "booking")
    }
}
