package com.example.kleenpride.admin.ui.bookings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.kleenpride.admin.ui.overview.AdminTopBar
import com.example.kleenpride.ui.theme.LimeGreen

//Data Class

data class AdminBooking(
    val id: String,
    val status1: String,
    val status1Color: Color,
    val status2: String,
    val status2Color: Color,
    val name: String,
    val detailer: String,
    val service: String,
    val car: String,
    val date: String,
    val price: String
)

//Main Screen

@Composable
fun AdminBookingsScreen() {

    var query by remember { mutableStateOf("") }

    //Hardcoded list for now
    val bookings = listOf(
        AdminBooking(
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
        ),
        AdminBooking(
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
        ),
        AdminBooking(
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
    )

    //Filter the List
    val filteredBookings = bookings.filter { booking ->
        val s = query.trim().lowercase()

        s.isEmpty() ||
                booking.id.lowercase().contains(s) ||
                booking.name.lowercase().contains(s) ||
                booking.detailer.lowercase().contains(s) ||
                booking.service.lowercase().contains(s) ||
                booking.car.lowercase().contains(s) ||
                booking.date.lowercase().contains(s)
    }

    Column(
        modifier = Modifier
            .background(Color.Black)
            .fillMaxSize()
    ) {
        AdminTopBar()

        SearchBookingsBar(
            query = query,
            onQueryChange = { query = it }
        )

        Spacer(Modifier.height(12.dp))

        // LazyColumn for proper scrolling & correct search behavior
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(filteredBookings) { booking ->
                AdminBookingCard(
                    id = booking.id,
                    status1 = booking.status1,
                    status1Color = booking.status1Color,
                    status2 = booking.status2,
                    status2Color = booking.status2Color,
                    name = booking.name,
                    detailer = booking.detailer,
                    service = booking.service,
                    car = booking.car,
                    date = booking.date,
                    price = booking.price
                )
            }
        }
    }
}

//Search Bar

@Composable
fun SearchBookingsBar(query: String, onQueryChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp)
            .background(Color(0xFF111111), shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = query,
            onValueChange = { onQueryChange(it) },
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

//Booking Card

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
    }
}

//Status Chip

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