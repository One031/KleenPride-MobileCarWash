package com.example.kleenpride.ui.booking

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kleenpride.data.booking.Booking

/**
 * Individual booking item in the list
 */
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
        }
    }
}