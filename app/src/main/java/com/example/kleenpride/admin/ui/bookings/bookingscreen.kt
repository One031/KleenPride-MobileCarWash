package com.example.kleenpride.admin.ui.bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kleenpride.admin.ui.overview.AdminTopBar
import com.example.kleenpride.ui.theme.LimeGreen

@Composable
fun AdminBookingsScreen() {
    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        AdminTopBar()
        SearchBookingsBar()

        Spacer(Modifier.height(12.dp))

        // Example list (you can replace with your API data)
        AdminBookingCard(
            id = "KP-2025-001",
            status1 = "ACTIVE",
            status1Color = LimeGreen,
            status2 = "PAID",
            status2Color = LimeGreen,
            name = "Jennifer Lopez",
            detailer = "James Smith",
            service = "Car Valet & Detailing",
            car = "Honda Civic 1996",
            date = "Nov 16, 2025, 10:00 AM",
            price = "R450"
        )

        AdminBookingCard(
            id = "KP-2025-002",
            status1 = "PENDING",
            status1Color = Color(0xFFFFC700),
            status2 = "PENDING",
            status2Color = Color(0xFFFFC700),
            name = "Bobby Brown",
            detailer = "Ja Rule",
            service = "Pride Wash",
            car = "Nissan 180SX 1998",
            date = "Nov 16, 2025, 2:30 PM",
            price = "R140"
        )

        AdminBookingCard(
            id = "KP-2025-003",
            status1 = "SCHEDULED",
            status1Color = Color(0xFF8A2BE2),
            status2 = "PENDING",
            status2Color = Color(0xFFFFC700),
            name = "Kelly Rowland",
            detailer = "Cornell Haynes",
            service = "Interior Detailing",
            car = "Mercedes-Benz 180E 1994",
            date = "Nov 17, 2024, 9:00 AM",
            price = "R55"
        )

        Spacer(Modifier.height(80.dp))
    }
}

@Composable
fun SearchBookingsBar() {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp)
            .background(Color(0xFF111111), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        var query by remember { mutableStateOf("") }

        BasicTextField(
            value = query,
            onValueChange = { query = it },
            singleLine = true,
            textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
            modifier = Modifier.weight(1f),
            decorationBox = { innerField ->
                if (query.isEmpty())
                    Text("Search bookings...", color = Color.Gray, fontSize = 16.sp)
                innerField()
            }
        )

        Icon(
            Icons.Default.FilterList,
            contentDescription = "Filter",
            tint = LimeGreen,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun AdminBookingCard(
    id: String,
    status1: String,
    status1Color: Color,
    status2: String,
    status2Color: Color,
    name: String,
    detailer: String,
    service: String,
    car: String,
    date: String,
    price: String
) {
    Column(
        modifier = Modifier
            .padding(12.dp)
            .background(Color(0xFF0F0F0F), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {

        // TOP ROW
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            Column {
                Row {
                    StatusChip(text = id, color = LimeGreen)
                    Spacer(Modifier.width(6.dp))
                    StatusChip(text = status1, color = status1Color)
                    Spacer(Modifier.width(6.dp))
                    StatusChip(text = status2, color = status2Color)
                }

                Spacer(Modifier.height(10.dp))

                Text("Customer", color = Color.Gray, fontSize = 13.sp)
                Text(name, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(6.dp))

                Text("Detailer", color = Color.Gray, fontSize = 13.sp)
                Text(detailer, color = Color.White)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(price, color = LimeGreen, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(10.dp))
                Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.Gray)
            }
        }

        Spacer(Modifier.height(12.dp))

        Text("$service • $car", color = Color.Gray, fontSize = 14.sp)
        Text(date, color = Color.Gray, fontSize = 13.sp)

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

            AdminActionButton(label = "View Details", icon = "👁")
            Spacer(Modifier.width(8.dp))
            AdminActionButton(label = "Edit", icon = "✏️")
        }
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

@Composable
fun RowScope.AdminActionButton(label: String, icon: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFF1A1A1A), RoundedCornerShape(10.dp))
            .padding(vertical = 10.dp)
            .weight(1f)
            .clickable { },
        contentAlignment = Alignment.Center
    ) {
        Text("$icon  $label", color = Color.White, fontWeight = FontWeight.Medium)
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
fun PreviewAdminBookingsScreen() {
    MaterialTheme {
        AdminBookingsScreen()
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
fun PreviewAdminBooking() {
    MaterialTheme {
        AdminBookingCard(
            id = "BK-2024-001",
            status1 = "ACTIVE",
            status1Color = LimeGreen,
            status2 = "PAID",
            status2Color = LimeGreen,
            name = "Sarah Johnson",
            detailer = "Leo Williams",
            service = "Premium Detail",
            car = "Honda Civic 2022",
            date = "Nov 16, 2024, 10:00 AM",
            price = "R2,750"
        )
    }
}