package com.example.kleenpride.admin.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.height
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.People

@Composable
fun AdminBottomNavBar(
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {
    val darkBackground = Color(0xFF0A0A0A)
    val limeGreen = Color(0xFF00FF66)
    val grayInactive = Color(0xFF808080)

    NavigationBar(
        containerColor = darkBackground,
        modifier = Modifier.height(65.dp)
    ) {

        NavigationBarItem(
            selected = selectedItem == "Overview",
            onClick = { onItemSelected("Overview") },
            icon = {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = "Overview",
                    tint = if (selectedItem == "Overview") limeGreen else grayInactive
                )
            },
            label = {
                Text(
                    "Overview",
                    color = if (selectedItem == "Overview") limeGreen else grayInactive
                )
            }
        )

        NavigationBarItem(
            selected = selectedItem == "Bookings",
            onClick = { onItemSelected("Bookings") },
            icon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Bookings",
                    tint = if (selectedItem == "Bookings") limeGreen else grayInactive
                )
            },
            label = {
                Text(
                    "Bookings",
                    color = if (selectedItem == "Bookings") limeGreen else grayInactive
                )
            }
        )

        NavigationBarItem(
            selected = selectedItem == "Detailers",
            onClick = { onItemSelected("Detailers") },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Detailers",
                    tint = if (selectedItem == "Detailers") limeGreen else grayInactive
                )
            },
            label = {
                Text(
                    "Detailers",
                    color = if (selectedItem == "Detailers") limeGreen else grayInactive
                )
            }
        )

        NavigationBarItem(
            selected = selectedItem == "Customers",
            onClick = { onItemSelected("Customers") },
            icon = {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = "Customers",
                    tint = if (selectedItem == "Customers") limeGreen else grayInactive
                )
            },
            label = {
                Text(
                    "Customers",
                    color = if (selectedItem == "Customers") limeGreen else grayInactive
                )
            }
        )
    }
}