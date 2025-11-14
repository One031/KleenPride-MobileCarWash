package com.example.kleenpride.detailers.ui.schedulescreen

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
import androidx.compose.foundation.rememberScrollState

@Composable
fun ScheduleScreen() {
    val darkBackground = Color(0xFF0A0A0A)
    val cardBackground = Color(0xFF141414)
    val neonGreen = Color(0xFF00FF66)
    val softGray = Color(0xFFCCCCCC)
    val mutedGreen = Color(0xFF1C2C1C)
    rememberScrollState()


    val jobs = listOf(
        JobItem("Sarah Johnson", "Premium Detail", "Honda Civic 2022", "10:00 AM • 4–5 hours", "123 Oak Street, Downtown", "R2,750", "3.7 km", "Today", true),
        JobItem("Mike Chen", "Basic Detail", "Toyota Camry 2021", "2:30 PM • 2–3 hours", "456 Maple Ave, West End", "R1,450", "2.9 km", "Today", true),
        JobItem("Jessica Brown", "Ultimate Detail", "BMW X5 2023", "9:00 AM • 6–8 hours", "789 Pine Road, Riverside", "R4,600", "6.6 km", "Tomorrow", false),
        JobItem("Robert Taylor", "Premium Detail", "Mercedes C-Class 2023", "11:00 AM • 4–5 hours", "321 Elm Street, Uptown", "R2,750", "5.1 km", "Oct 31", true),
        JobItem("Emily Davis", "Basic Detail", "Ford Focus 2020", "3:00 PM • 2–3 hours", "654 Cedar Lane, South Side", "R1,450", "4.5 km", "Nov 1", true)
    )


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 70.dp)
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("My Schedule", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                IconButton(onClick = { }) {
                    Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = neonGreen)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Week Overview Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardBackground, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text("This Week", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("5 bookings scheduled", color = softGray, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        listOf("M", "T", "W", "T", "F", "S", "S").forEachIndexed { index, day ->
                            val isActive = index in 0..4
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 2.dp)
                                    .background(
                                        if (isActive) mutedGreen else Color(0xFF1A1A1A),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .height(36.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(day, color = if (isActive) neonGreen else softGray)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("All Upcoming Jobs", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(jobs) { job ->
                    JobCard(job, cardBackground, neonGreen, softGray)
                }
            }
        }

    }
}

data class JobItem(
    val name: String,
    val detailType: String,
    val car: String,
    val time: String,
    val address: String,
    val price: String,
    val distance: String,
    val day: String,
    val confirmed: Boolean
)

@Composable
fun JobCard(job: JobItem, background: Color, accent: Color, softGray: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = background),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(job.day, color = accent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(job.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(job.detailType, color = softGray, fontSize = 13.sp)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(job.price, color = accent, fontWeight = FontWeight.Bold)
                    Text(job.distance, color = softGray, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = null, tint = softGray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(job.time, color = softGray, fontSize = 12.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DirectionsCar, contentDescription = null, tint = softGray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(job.car, color = softGray, fontSize = 12.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = accent, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(job.address, color = softGray, fontSize = 12.sp)
            }

            if (job.day == "Tomorrow" && !job.confirmed) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = accent),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp)
                    ) {
                        Text("Accept", color = Color.Black, fontWeight = FontWeight.Bold)
                    }
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E1E1E)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp)
                    ) {
                        Text("Decline", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            } else if (job.confirmed) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = accent, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Confirmed", color = accent, fontSize = 13.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScheduleScreenPreview() {
    ScheduleScreen()
}
