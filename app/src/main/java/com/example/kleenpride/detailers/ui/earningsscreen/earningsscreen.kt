package com.example.kleenpride.detailers.ui.earningsscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Notifications

data class Payment(val date: String, val jobs: String, val amount: String, val status: String)

@Composable
fun EarningsScreen() {
    val darkBackground = Color(0xFF0A0A0A)
    val cardBackground = Color(0xFF141414)
    val neonGreen = Color(0xFF00FF66)
    val softGray = Color(0xFFCCCCCC)
    val orange = Color(0xFFFFA500)

    val payments = listOf(
        Payment("Oct 29", "3 jobs", "R4,200", "Pending"),
        Payment("Oct 28", "1 job", "R4,600", "Paid"),
        Payment("Oct 27", "2 jobs", "R4,365", "Paid"),
        Payment("Oct 26", "4 jobs", "R5,820", "Paid")
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("My Earnings", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = neonGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            item {
                // Stat Cards Row 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatCard("Today", "R4,200", cardBackground, neonGreen)
                    StatCard("This Week", "R22,800", cardBackground, neonGreen)
                }
            }

            item {
                // Stat Cards Row 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatCard("This Month", "R89,350", cardBackground, neonGreen)
                    StatCard("All Time", "R345,600", cardBackground, neonGreen)
                }
            }

            item {
                // Weekly Breakdown
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = cardBackground),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Weekly Breakdown", color = Color.White, fontWeight = FontWeight.Bold)
                            Icon(
                                imageVector = Icons.Default.BarChart,
                                contentDescription = "Breakdown",
                                tint = neonGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri")
                        val earnings = listOf("R2,910", "R4,365", "R5,820", "R5,545", "R4,200")

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            days.zip(earnings).forEach { (day, value) ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        modifier = Modifier
                                            .width(50.dp)
                                            .height(20.dp)
                                            .background(neonGreen, RoundedCornerShape(4.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            value,
                                            fontSize = 10.sp,
                                            color = Color.Black,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(day, color = softGray, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text("Recent Payments", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            items(payments) { payment ->
                PaymentCard(payment, cardBackground, neonGreen, orange)
            }

            item {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}


@Composable
fun StatCard(title: String, amount: String, background: Color, accent: Color) {
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
            Text(title, color = Color.White, fontSize = 13.sp)
            Text(amount, color = accent, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun PaymentCard(payment: Payment, background: Color, accent: Color, orange: Color) {
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
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(payment.date, color = Color.White, fontWeight = FontWeight.Bold)
                Text(payment.jobs, color = Color.Gray, fontSize = 13.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(payment.amount, color = accent, fontWeight = FontWeight.Bold)
                Text(
                    payment.status,
                    color = if (payment.status == "Paid") accent else orange,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EarningsScreenPreview() {
    EarningsScreen()
}
