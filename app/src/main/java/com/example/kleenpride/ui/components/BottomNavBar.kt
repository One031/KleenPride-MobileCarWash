package com.example.kleenpride.ui.components

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.EventNote
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.kleenpride.ui.homescreen.MainActivity
import com.example.kleenpride.ui.booking.BookingActivity
import com.example.kleenpride.ui.alerts.AlertsActivity
import com.example.kleenpride.ui.profile.ProfileActivity
import com.example.kleenpride.ui.theme.LimeGreen

@Composable
fun BottomNavBar(currentScreen: String) {
    val context = LocalContext.current

    NavigationBar(containerColor = Color.Black) {

        // Home
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

        // Booking
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
                    Icons.AutoMirrored.Filled.EventNote,
                    contentDescription = "Booking",
                    tint = if (currentScreen == "booking") LimeGreen else Color.White
                )
            },
            label = { Text("Booking", color = Color.White) }
        )

        // Alerts
        NavigationBarItem(
            selected = currentScreen == "alerts",
            onClick = {
                if (currentScreen != "alerts") {
                    val intent = Intent(context, AlertsActivity::class.java)
                    context.startActivity(intent)
                }
            },
            icon = {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Alerts",
                    tint = if (currentScreen == "alerts") LimeGreen else Color.White
                )
            },
            label = { Text("Alerts", color = Color.White) }
        )

        // Profile
        NavigationBarItem(
            selected = currentScreen == "profile",
            onClick = {
                if (currentScreen != "profile") {
                    val intent = Intent(context, ProfileActivity::class.java)
                    context.startActivity(intent)
                }
            },
            icon = {
                Icon(
                    Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = if (currentScreen == "profile") LimeGreen else Color.White
                )
            },
            label = { Text("Profile", color = Color.White) }
        )

    }
}