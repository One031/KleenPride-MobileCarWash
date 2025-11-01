package com.example.kleenpride.detailers.ui.homescreen

import androidx.activity.ComponentActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kleenpride.detailers.ui.components.BottomNavBar
import com.example.kleenpride.detailers.ui.profile.ProfileScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DetailerMainScreen()
        }
    }
}

@Composable
fun DetailerMainScreen() {
    val darkBackground = Color(0xFF0A0A0A)
    var selectedTab by remember { mutableStateOf("Dashboard") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
    ) {
        // --- Main Content based on selected tab ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp) // Prevent content from overlapping navbar
        ) {
            when (selectedTab) {
                "Dashboard" -> DetailerDashboardScreen()
                "Schedule" -> ScheduleScreen()
                "Earnings" -> EarningsScreen()
                "Profile" -> ProfileScreen()
            }
        }

        // --- Bottom Navigation Bar ---
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavBar(
                selectedItem = selectedTab,
                onItemSelected = { selectedTab = it }
            )
        }
    }
}

@Composable
fun ScheduleScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A)),
        contentAlignment = Alignment.Center
    ) {
        Text("Schedule Screen (Coming Soon)", color = Color.White)
    }
}

@Composable
fun EarningsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0A0A)),
        contentAlignment = Alignment.Center
    ) {
        Text("Earnings Screen (Coming Soon)", color = Color.White)
    }
}

@Composable
fun DetailerDashboardScreen() {
    val darkBackground = Color(0xFF0A0A0A)
    val cardBackground = Color(0xFF141414)
    val neonGreen = Color(0xFF00FF66)
    val softGray = Color(0xFFCCCCCC)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Detailer Dashboard", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("Welcome back, Leo!", color = softGray, fontSize = 14.sp)
            }
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(neonGreen, RoundedCornerShape(50)),
                contentAlignment = Alignment.Center
            ) {
                Text("L", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatCard("Today's Jobs", "5", Icons.Default.CalendarToday, cardBackground, neonGreen)
            StatCard("Week Earnings", "R1240", Icons.Default.AttachMoney, cardBackground, neonGreen)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatCard("Completed", "127", Icons.Default.CheckCircle, cardBackground, neonGreen)
            StatCard("Rating", "4.8", Icons.Default.Star, cardBackground, neonGreen)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Progress / Bonus Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0D1F0D), RoundedCornerShape(10.dp))
                .padding(16.dp)
        ) {
            Column {
                Text("You're doing great!", color = neonGreen, fontWeight = FontWeight.Bold)
                Text("3 more jobs to reach weekly bonus of R100", color = softGray, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Upcoming Bookings
        Text("Upcoming Bookings", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        val bookings = listOf(
            Booking("Debelah Morgan", "Premium Detail", "Mercedes-Benz 190E 1998", "10:00 AM - 4-5 hours", "123 Oak Street, Downtown", "R149"),
            Booking("Bobby Valentino", "Basic Detail", "Nissan 200SX 1992", "2:30 PM - 2-3 hours", "456 Maple Ave, West End", "R79"),
            Booking("Raphael Saadiq", "Ultimate Detail", "Honda Civic 1996", "9:00 AM - 6-8 hours", "789 Pine Road, Riverside", "R249")
        )

        LazyColumn {
            items(bookings) { booking ->
                BookingCard(booking, cardBackground, neonGreen)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recent Completed
        Text("Recent Completed", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        val completed = listOf(
            CompletedJob("David Lee", "Premium Detail", "+R149", "2 hours ago"),
            CompletedJob("Amy Wilson", "Basic Detail", "+R79", "5 hours ago"),
            CompletedJob("Chris Taylor", "Ultimate Detail", "+R249", "Yesterday")
        )

        LazyColumn {
            items(completed) { job ->
                CompletedJobCard(job, cardBackground, neonGreen)
            }
        }
    }
}

@Composable
fun StatCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, background: Color, accent: Color) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(80.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(title, color = Color.White, fontSize = 12.sp)
            }
            Text(value, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}

data class Booking(val name: String, val type: String, val car: String, val time: String, val address: String, val price: String)
data class CompletedJob(val name: String, val type: String, val earnings: String, val timeAgo: String)

@Composable
fun BookingCard(booking: Booking, background: Color, accent: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(booking.name, color = Color.White, fontWeight = FontWeight.Bold)
                    Text(booking.type, color = accent, fontSize = 13.sp)
                }
                Text(booking.price, color = accent, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${booking.car}", color = Color.Gray, fontSize = 12.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(booking.time, color = Color.Gray, fontSize = 12.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = accent, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(booking.address, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun CompletedJobCard(job: CompletedJob, background: Color, accent: Color) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(job.name, color = Color.White, fontWeight = FontWeight.Bold)
                Text(job.type, color = Color.Gray, fontSize = 13.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(job.earnings, color = accent, fontWeight = FontWeight.Bold)
                Text(job.timeAgo, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailerDashboardPreview() {
    DetailerMainScreen()
}
