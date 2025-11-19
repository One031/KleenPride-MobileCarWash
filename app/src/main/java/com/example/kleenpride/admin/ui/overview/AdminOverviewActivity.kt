package com.example.kleenpride.admin.ui.overview

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kleenpride.ui.theme.LimeGreen
import com.example.kleenpride.admin.ui.components.AdminBottomNavBar
import com.example.kleenpride.admin.ui.bookings.AdminBookingsScreen
import com.example.kleenpride.admin.ui.detailers.AdminDetailersScreen
import com.example.kleenpride.admin.ui.customers.AdminCustomersScreen
import com.example.kleenpride.admin.ui.profile.ProfileScreenActivity
import androidx.compose.foundation.shape.CircleShape

class AdminOverviewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AdminMainScreen()
        }
    }
}

@Composable
fun AdminMainScreen() {
    val darkBackground = Color(0xFF0A0A0A)
    var selectedTab by remember { mutableStateOf("Overview") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp)
        ) {
            when (selectedTab) {
                "Overview" -> AdminOverviewScreen()
                "Bookings" -> AdminBookingsScreen()
                "Detailers" -> AdminDetailersScreen()
                "Customers" -> AdminCustomersScreen()
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        ) {
            AdminBottomNavBar(
                selectedItem = selectedTab,
                onItemSelected = { selectedTab = it }
            )
        }
    }
}

@Composable
fun AdminOverviewScreen() {
    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AdminTopBar()
        AdminStatsSection()
        TodayPerformanceCard()
        RecentBookingsSection()
        Spacer(Modifier.height(80.dp))
    }
}

@Composable
fun AdminTopBar() {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White)
            Spacer(Modifier.width(12.dp))

            Column {
                Text("Admin Dashboard", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text("KleenPride Management", color = Color.Gray, fontSize = 13.sp)
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Settings, contentDescription = null, tint = Color.White)
            Spacer(Modifier.width(16.dp))

            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(LimeGreen)
                    .clickable {
                        context.startActivity(
                            Intent(context, ProfileScreenActivity::class.java)
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("A", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun AdminStatsSection() {
    Column(Modifier.padding(16.dp)) {

        Row(Modifier.fillMaxWidth()) {
            StatCard(
                title = "Total Revenue",
                value = "R28,255",
                sub = "+12% this month",
                borderColor = LimeGreen
            )
            Spacer(Modifier.width(12.dp))
            StatCard(
                title = "Active Bookings",
                value = "13",
                sub = "Today: 6 completed"
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(Modifier.fillMaxWidth()) {
            StatCard(
                title = "Total Detailers",
                value = "3",
                sub = "1 active now"
            )
            Spacer(Modifier.width(12.dp))
            StatCard(
                title = "Total Customers",
                value = "23",
                sub = "23 registered"
            )
        }
    }
}

@Composable
fun RowScope.StatCard(
    title: String,
    value: String,
    sub: String,
    borderColor: Color = Color.DarkGray
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .background(Color(0xFF111111), shape = MaterialTheme.shapes.medium)
            .border(1.dp, borderColor, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Text(title, color = Color.Gray, fontSize = 13.sp)
        Spacer(Modifier.height(8.dp))
        Text(value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text(sub, color = LimeGreen, fontSize = 13.sp)
    }
}

@Composable
fun TodayPerformanceCard() {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color(0xFF111111), shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Text("Today's Performance", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(16.dp))

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Revenue", color = Color.Gray)
                Text("R1250", color = LimeGreen, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Column {
                Text("Completed", color = Color.Gray)
                Text("6", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Column {
                Text("Pending", color = Color.Gray)
                Text("3", color = Color.Yellow, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Column {
                Text("Scheduled", color = Color.Gray)
                Text("4", color = Color.Magenta, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RecentBookingsSection() {
    Column(Modifier.padding(16.dp)) {
        Text("Recent Bookings", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        BookingCard(
            bookingId = "KP-2025-001",
            status = "ACTIVE",
            statusColor = LimeGreen,
            name = "Jennifer Lopez",
            detailer = "James Smith",
            price = "R450",
            paid = true,
            car = "Honda Civic 1996",
            date = "Nov 16, 2025, 10:00 AM",
            service = "Car Valet & Detailing"
        )

        Spacer(Modifier.height(12.dp))

        BookingCard(
            bookingId = "KP-2025-002",
            status = "PENDING",
            statusColor = Color(0xFFFFC700),
            name = "Bobby Brown",
            detailer = "Ja Rule",
            price = "R140",
            paid = false,
            car = "Nissan 180SX 1998",
            date = "Nov 16, 2025, 2:30 PM",
            service = "Pride Wash"
        )

        Spacer(Modifier.height(12.dp))

        BookingCard(
            bookingId = "KP-2025-003",
            status = "SCHEDULED",
            statusColor = Color(0xFF8A2BE2),
            name = "Kelly Rowland",
            detailer = "Cornell Haynes",
            price = "R55",
            paid = false,
            car = "Mercedes-Benz 180E 1994",
            date = "Nov 17, 2025, 9:00 AM",
            service = "Interior Detailing"
        )
    }
}

@Composable
fun BookingCard(
    bookingId: String,
    status: String,
    statusColor: Color,
    name: String,
    detailer: String,
    price: String,
    paid: Boolean,
    car: String,
    date: String,
    service: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF111111), shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text("$bookingId • $status", color = statusColor, fontWeight = FontWeight.Bold)
                Text(name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("Service by $detailer", color = Color.Gray)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(price, color = LimeGreen, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                if (paid) Text("✓ Paid", color = LimeGreen)
                else Text("Pending", color = Color.Yellow)
            }
        }

        Spacer(Modifier.height(8.dp))
        Text("$car   •   $date", color = Color.Gray, fontSize = 13.sp)
        Spacer(Modifier.height(8.dp))
        Text(service, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0x000000
)
@Composable
fun AdminOverviewPreview() {
    AdminMainScreen()
}