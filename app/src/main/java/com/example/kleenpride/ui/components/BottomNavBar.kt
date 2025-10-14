package com.example.kleenpride.ui.components


import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.kleenpride.ui.homescreen.MainActivity
import com.example.kleenpride.ui.booking.BookingActivity
import com.example.kleenpride.ui.theme.LimeGreen

@Composable
fun BottomNavBar(currentScreen: String) {
    val context = LocalContext.current

    // Home
    NavigationBar(containerColor = Color.Black) {
        NavigationBarItem(
            selected = currentScreen == "home",
            onClick = {
                if (currentScreen != "home") {
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
            },

            icon = {
                Icon(
                    Icons.Default.Home,
                    contentDescription = "Home",
                    tint = if (currentScreen == "home") LimeGreen else Color.White
                )
            },
            label = { Text("Home", color = Color.White) }
        )

        // Bookings
        NavigationBarItem(
            selected = currentScreen == "booking",
            onClick = {
                if (currentScreen != "booking") {
                    val intent = Intent(context, BookingActivity::class.java)
                    context.startActivity(intent)
                }
            },
            icon = {
                Icon(
                    Icons.Default.EventNote,
                    contentDescription = "Booking",
                    tint = if (currentScreen == "booking") LimeGreen else Color.White
                )
            },
            label = { Text("Booking", color = Color.White) }
        )
    }
}