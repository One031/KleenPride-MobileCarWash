package com.example.kleenpride.admin.ui.customers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.kleenpride.admin.ui.overview.AdminTopBar
import com.example.kleenpride.ui.theme.LimeGreen

class AdminCustomersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AdminCustomersScreen() }
    }
}

@Composable
fun AdminCustomersScreen() {
    Scaffold(
        containerColor = Color.Black,
        topBar = { AdminTopBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(Color.Black)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            CustomersHeader()

            Spacer(Modifier.height(12.dp))

            // Example customers list
            CustomerCard(
                name = "Jennifer Lopez",
                email = "Jenniferlopez@gmail.com",
                phone = "+27 71 123 4567",
                totalBookings = 15,
                totalSpent = 1200,
                lastBooking = "Nov 16, 2025",
                status = "ACTIVE",
                statusColor = LimeGreen
            )

            CustomerCard(
                name = "Bobby Brown",
                email = "BobbyBrown@gmail.com",
                phone = "+27 72 234 5678",
                totalBookings = 8,
                totalSpent = 1100,
                lastBooking = "Nov 15, 2024",
                status = "ACTIVE",
                statusColor = LimeGreen
            )

            CustomerCard(
                name = "Kelly Rowland",
                email = "Kelly.Rowland@gmail.com",
                phone = "+27 73 345 6789",
                totalBookings = 22,
                totalSpent = 1500,
                lastBooking = "Nov 14, 2024",
                status = "ACTIVE",
                statusColor = LimeGreen
            )

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
fun CustomersHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                "All Customers",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "4 registered",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun CustomerCard(
    name: String,
    email: String,
    phone: String,
    totalBookings: Int,
    totalSpent: Int,
    lastBooking: String,
    status: String,
    statusColor: Color
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .background(Color(0xFF0F0F0F), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {

        // Profile Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(verticalAlignment = Alignment.Top) {
                // Profile Picture
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFF2A2A2A), RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        name.first().toString(),
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            name,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.width(8.dp))
                        StatusChip(text = status, color = statusColor)
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(
                        email,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )

                    Text(
                        phone,
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }

            Icon(
                Icons.Default.MoreVert,
                contentDescription = "More options",
                tint = Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(Modifier.height(16.dp))

        // Stats Grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomerStatBox(
                label = "Bookings",
                value = totalBookings.toString(),
                valueColor = Color.White
            )

            Spacer(Modifier.width(8.dp))

            Column(
                modifier = Modifier
                    .weight(2f)
                    .background(Color(0xFF0A0A0A), RoundedCornerShape(8.dp))
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Total Spent", color = Color.Gray, fontSize = 11.sp)
                Spacer(Modifier.height(4.dp))
                Text(
                    "R${String.format("%,d", totalSpent)}",
                    color = LimeGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        // Last Booking Info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF0A0A0A), RoundedCornerShape(8.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.CalendarToday,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Last booking: ",
                color = Color.Gray,
                fontSize = 12.sp
            )
            Text(
                lastBooking,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(16.dp))


    }
}

@Composable
fun RowScope.CustomerStatBox(
    label: String,
    value: String,
    valueColor: Color
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .background(Color(0xFF0A0A0A), RoundedCornerShape(8.dp))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, color = Color.Gray, fontSize = 11.sp)
        Spacer(Modifier.height(4.dp))
        Text(
            value,
            color = valueColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RowScope.CustomerActionButton(label: String) {
    Box(
        modifier = Modifier
            .weight(1f)
            .background(Color(0xFF1A1A1A), RoundedCornerShape(10.dp))
            .padding(vertical = 10.dp)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 14.sp)
    }
}

@Composable
fun StatusChip(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.2f), shape = RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
fun PreviewAdminCustomersScreen() {
    MaterialTheme {
        AdminCustomersScreen()
    }
}

