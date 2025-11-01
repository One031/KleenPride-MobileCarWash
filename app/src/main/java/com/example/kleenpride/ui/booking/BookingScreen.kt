package com.example.kleenpride.ui.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
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
fun BookingScreen(viewModel: BookingViewModel = viewModel() ) {

   // Observe bookings list from the ViewModel
    val bookings by viewModel.bookings.observeAsState(emptyList())

    // If there are no bookings in the database show the empty screen
    if (bookings.isEmpty()) {
        EmptyBookingScreen()
        return
    }

    // Find the active booking if available
    val activeBooking = bookings.firstOrNull { it.status == "Active" }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Page Title
        Text(
            text = "Bookings",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        // Active Booking tile
        if (activeBooking != null){
            ActiveBookingCard(activeBooking)
        } else {
            NoActiveBookingCard()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Booking Button
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 7.dp) // align with rest of layout
        ) {
            Button(
                onClick =  { /* handle click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LimeGreen
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon (
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
            verticalArrangement = Arrangement.spacedBy(12.dp), // spacing between bookings
            modifier = Modifier.fillMaxWidth()
        ) {
            items(bookings.take(3)) { booking -> // show only 3 bookings
                BookingItem(booking)
            }
        }

        // Making the NavBar be at the bottom
        Spacer(modifier = Modifier.weight(1f))

        // Sharing the navbar with the booking icon
        BottomNavBar(currentScreen = "booking")
    }
}


@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
fun BookingScreenPreview() {
   val mockBookings = listOf(
       Booking("B001", "Active", "Wash & Go", "Oct 10, 2025 at 1:25 PM", "R120"),
       Booking("B002", "Completed", "Full Detail", "Oct 6, 2025", "R250"),
       Booking("B003", "Completed", "Quick Wash", "Oct 4, 2025", "R100")
   )

    // Call the screen with sample bookings, not the real ViewModel
    BookingScreenPreviewContent(mockBookings)
}

@Composable
fun BookingScreenPreviewContent(bookings: List<Booking>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Text(
            text = "Bookings",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(20.dp))

        ActiveBookingCard(bookings.first())
        Spacer(modifier = Modifier.height(20.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 7.dp) // align with rest of layout
        ) {
            Button(
                onClick =  { /* handle click */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LimeGreen
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon (
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


        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Recent Bookings",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        bookings.drop(1).take(2).forEach {
            BookingItem(it)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}