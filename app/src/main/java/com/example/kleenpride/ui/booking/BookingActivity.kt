package com.example.kleenpride.ui.booking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kleenpride.ui.theme.LimeGreen
import com.example.kleenpride.ui.components.BottomNavBar

class BookingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BookingScreen()
        }
    }
}

@Composable
fun BookingScreen() {
    val bookings = remember {
        mutableStateListOf(
            Booking("Pride Wash", "Oct 7, 2025", "R120", "Completed", "KP-0725"),
            Booking("Car Valet & Detailing", "Oct 5, 2025", "R450", "Completed", "KP-0525")
        )
    }

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
        ActiveBookingCard()

        Spacer(modifier = Modifier.height(16.dp))

        // Booking Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF00C853))
                .clickable { }
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Create New Booking",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
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

        LazyColumn {
            items(bookings) { booking ->
                BookingItem(booking)
            }
        }

        // Making the NavBar be at the bottom
        Spacer(modifier = Modifier.weight(1f))

        // Sharing the navbar with the booking icon
        BottomNavBar(currentScreen = "booking")
    }
}

// --- Booking Data Model ---
data class Booking(
    val title: String,
    val date: String,
    val price: String,
    val status: String,
    val id: String
)

// Active Booking Tile
@Composable
fun ActiveBookingCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF1B1B1B))
            .padding(16.dp)
    ) {
        Text(
            text = "Active Booking",
            color = Color(0xFF00C853),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Wash & Go",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Oct 10, 2025 at 1:25 PM",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = "ETA",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Text(
                    text = "21 mins",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Track booking →",
                color = Color(0xFF00C853),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }
}

// Recent Bookings Section
@Composable
fun BookingItem(booking: Booking) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF1B1B1B))
            .padding(16.dp)
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "✔ ",
                    color = Color(0xFF00C853),
                    fontSize = 14.sp
                )
                Text(
                    text = booking.status,
                    color = Color(0xFF00C853),
                    fontSize = 14.sp
                )
            }
            Text(
                text = booking.title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = booking.date,
                color = Color.Gray,
                fontSize = 13.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = booking.price,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = booking.id,
                color = Color.Gray,
                fontSize = 13.sp
            )
            Text(
                text = "Details",
                color = Color(0xFF00C853),
                fontSize = 14.sp,
                modifier = Modifier.clickable { }
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
fun BookingScreenPreview() {
    BookingScreen()
}