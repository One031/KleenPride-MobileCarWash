package com.example.kleenpride.detailers.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Person

@Composable
fun BottomNavBar(
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    val darkBackground = Color(0xFF0A0A0A)
    val neonGreen = Color(0xFF00FF66)
    val grayInactive = Color(0xFF808080)

    NavigationBar(
        containerColor = darkBackground,
        modifier = Modifier.height(65.dp)
    ) {
        NavigationBarItem(
            selected = selectedItem == "Dashboard",
            onClick = { onItemSelected("Dashboard") },
            icon = {
                Icon(
                    imageVector = Icons.Default.ShowChart,
                    contentDescription = "Dashboard",
                    tint = if (selectedItem == "Dashboard") neonGreen else grayInactive
                )
            },
            label = {
                Text(
                    "Dashboard",
                    color = if (selectedItem == "Dashboard") neonGreen else grayInactive
                )
            }
        )
        NavigationBarItem(
            selected = selectedItem == "Schedule",
            onClick = { onItemSelected("Schedule") },
            icon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Schedule",
                    tint = if (selectedItem == "Schedule") neonGreen else grayInactive
                )
            },
            label = {
                Text(
                    "Schedule",
                    color = if (selectedItem == "Schedule") neonGreen else grayInactive
                )
            }
        )
        NavigationBarItem(
            selected = selectedItem == "Earnings",
            onClick = { onItemSelected("Earnings") },
            icon = {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = "Earnings",
                    tint = if (selectedItem == "Earnings") neonGreen else grayInactive
                )
            },
            label = {
                Text(
                    "Earnings",
                    color = if (selectedItem == "Earnings") neonGreen else grayInactive
                )
            }
        )
        NavigationBarItem(
            selected = selectedItem == "Profile",
            onClick = { onItemSelected("Profile") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = if (selectedItem == "Profile") neonGreen else grayInactive
                )
            },
            label = {
                Text(
                    "Profile",
                    color = if (selectedItem == "Profile") neonGreen else grayInactive
                )
            }
        )
    }
}