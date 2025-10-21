package com.example.kleenpride.ui.alerts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kleenpride.ui.components.BottomNavBar
import com.example.kleenpride.ui.theme.LimeGreen

class AlertsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AlertsScreen()
        }
    }
}

@Composable
fun AlertsScreen() {
    val alerts = remember {
        listOf(
            Alert("Booking Confirmed", "Your Pride Wash booking is confirmed for Oct 18, 10:00 AM", "5 mins ago", true),
            Alert("Upcoming Service", "Your Car Valet is scheduled for tomorrow at 2:30 PM", "2 hours ago", true),
            Alert("Payment Reminder", "Payment of R150 is pending for your last service.", "Yesterday"),
            Alert("Service Completed", "Your Wash & Go service has been completed.", "Yesterday"),
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Alerts",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Mark all read",
                color = LimeGreen,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable { /* Mark as read */ }
            )
        }

        Text(
            text = "Stay updated with your bookings and offers",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 6.dp, bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(alerts) { alert ->
                AlertCard(alert)
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1B1B1B))
                        .clickable { /* Clear all */ }
                        .padding(vertical = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Clear All Alerts",
                        color = LimeGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // Navigation bar stays at the bottom
        BottomNavBar(currentScreen = "alerts")
    }
}

// Alert Data Model
data class Alert(
    val title: String,
    val message: String,
    val time: String,
    val isHighlighted: Boolean = false
)

// Alert notifications
@Composable
fun AlertCard(alert: Alert) {
    val borderColor = if (alert.isHighlighted) LimeGreen else Color.Transparent
    val backgroundColor = if (alert.isHighlighted) Color(0xFF0A1F0A) else Color(0xFF1B1B1B)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = alert.title,
            color = if (alert.isHighlighted) LimeGreen else Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = alert.message,
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = alert.time,
            color = Color(0xFF7F7F7F),
            fontSize = 12.sp
        )
    }
}

// Preview
@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
fun AlertsScreenPreview() {
    AlertsScreen()
}