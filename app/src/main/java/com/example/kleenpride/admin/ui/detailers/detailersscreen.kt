package com.example.kleenpride.admin.ui.detailers

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

class AdminDetailersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AdminDetailersScreen() }
    }
}

@Composable
fun AdminDetailersScreen() {
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

            DetailersHeader()

            Spacer(Modifier.height(12.dp))

            // Example detailers list
            DetailerCard(
                name = "James Smith",
                email = "James.smith@kleenpride.com",
                phone = "+27 82 123 4567",
                rating = 4.8f,
                totalJobs = 38,
                earnings = 21000,
                status = "ACTIVE",
                statusColor = LimeGreen,
                joinDate = "Jan 2024"
            )

            DetailerCard(
                name = "Ja Rule",
                email = "Ja.rule@kleenpride.com",
                phone = "+27 83 234 5678",
                rating = 4.6f,
                totalJobs = 46,
                earnings = 18000,
                status = "ACTIVE",
                statusColor = LimeGreen,
                joinDate = "Feb 2024"
            )

            DetailerCard(
                name = "Cornell Haynes",
                email = "Cornell.haynes@kleenpride.com",
                phone = "+27 84 345 6789",
                rating = 4.9f,
                totalJobs = 51,
                earnings = 25000,
                status = "ACTIVE",
                statusColor = LimeGreen,
                joinDate = "Dec 2023"
            )

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
fun DetailersHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                "All Detailers",
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

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = LimeGreen),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("+ Add Detailer", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DetailerCard(
    name: String,
    email: String,
    phone: String,
    rating: Float,
    totalJobs: Int,
    earnings: Int,
    status: String,
    statusColor: Color,
    joinDate: String
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
                        .background(LimeGreen, RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        name.first().toString(),
                        color = Color.Black,
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
            DetailerStatBox(
                label = "Rating",
                value = rating.toString(),
                valueColor = Color.White,
                icon = Icons.Default.Star
            )

            DetailerStatBox(
                label = "Jobs",
                value = totalJobs.toString(),
                valueColor = Color.White,
                icon = null
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF0A0A0A), RoundedCornerShape(8.dp))
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Earnings", color = Color.Gray, fontSize = 11.sp)
                Spacer(Modifier.height(4.dp))
                Text(
                    "R${String.format("%,d", earnings)}",
                    color = LimeGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DetailerActionButton(label = "View Profile")
            Spacer(Modifier.width(8.dp))
            DetailerActionButton(label = "Edit")
            Spacer(Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .background(
                        if (status == "ACTIVE") Color(0xFF220000) else Color(0xFF002200),
                        RoundedCornerShape(10.dp)
                    )
                    .padding(vertical = 10.dp, horizontal = 12.dp)
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    if (status == "ACTIVE") Icons.Default.Close else Icons.Default.Check,
                    contentDescription = if (status == "ACTIVE") "Deactivate" else "Activate",
                    tint = if (status == "ACTIVE") Color.Red else LimeGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun RowScope.DetailerStatBox(
    label: String,
    value: String,
    valueColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector?
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .background(Color(0xFF0A0A0A), RoundedCornerShape(8.dp))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = LimeGreen,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(4.dp))
            }
            Text(label, color = Color.Gray, fontSize = 11.sp)
        }
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
fun RowScope.DetailerActionButton(label: String) {
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
fun PreviewAdminDetailersScreen() {
    MaterialTheme {
        AdminDetailersScreen()
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
fun PreviewDetailerCard() {
    MaterialTheme {
        DetailerCard(
            name = "Ja Rule",
            email = "Ja.rule@kleenpride.com",
            phone = "+27 82 123 4567",
            rating = 4.8f,
            totalJobs = 46,
            earnings = 18000,
            status = "ACTIVE",
            statusColor = LimeGreen,
            joinDate = "Jan 2024"
        )
    }
}