package com.example.kleenpride.ui.homescreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat // daily time update so that it shows the current day, date, year
import java.util.*

//Change as much as you want, every functionality is here, by "//" you can null a line or code, so that you dont have to delete it.
/* HOME SCREEN */
@Composable
fun HomeScreen() {
    val userName = "John Doe" // for now, user name should be fetched
    val initials = userName.split(" ").map { it.first() }.joinToString("").uppercase()// profile button, with initials of username
    val context = LocalContext.current
    val today = SimpleDateFormat("d MMMM yyyy", Locale.getDefault()).format(Date())//Gets updated time on daily

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(16.dp)
    ) {
        // Greeting Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Hi, $userName!", // The user name should be fetched from database
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = today,
                    color = Color.Gray,
                    fontSize = 18.sp
                )
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(50.dp)
                    .background(Color.DarkGray, shape = CircleShape)
                    .border(2.dp, Color.Green, CircleShape)
            ) {
                Text(initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }

        // Notifications Box, isnt clickable, but can be turned into what you desire
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(top = 8.dp)
                .background(Color(0xFF101010), shape = RoundedCornerShape(12.dp))
                .border(1.dp, Color(0xFFDDDDDD), shape = RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Text(
                text = "Notification messages",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }

        // Two Action Boxes Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Make Booking Box, when clicked needs to take you to service screen
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(160.dp)
                    .background(Color(0xFF101010), shape = RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFDDDDDD), shape = RoundedCornerShape(12.dp))
                    .clickable {
                        Toast.makeText(context, "Make Booking clicked", Toast.LENGTH_SHORT).show()
                    }
                    .padding(12.dp)
            ) {
                Text(
                    text = "Make Booking",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // View Appointments Box, its up to you to decide what happens when clicked
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(160.dp)
                    .background(Color(0xFF101010), shape = RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFDDDDDD), shape = RoundedCornerShape(12.dp))
                    .clickable {
                        Toast.makeText(context, "View Appointments clicked", Toast.LENGTH_SHORT).show()
                    }
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "View Appointments:",
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = today,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Review Section, when clicked, allow it to take user to a review screen with stars and comments
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Happy with our services? Why not leave a ",
                    color = Color.White,
                    fontSize = 15.sp
                )
                Text(
                    text = "Review", //when clicked, allow it to take user to a review screen with stars and comments
                    color = Color.Green,
                    fontSize = 15.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Review clicked", Toast.LENGTH_SHORT).show()
                    }
                )
                Text(
                    text = "?",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            Box(//this where the google maps display supposed to be
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(top = 8.dp)
                    .background(Color(0xFF101010), shape = RoundedCornerShape(12.dp))
                    .border(1.dp, Color(0xFFDDDDDD), shape = RoundedCornerShape(12.dp))
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Bottom Navigation
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                Toast.makeText(context, "Back clicked", Toast.LENGTH_SHORT).show()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            IconButton(onClick = { // have this home button on all pages, you can null the arrow back and home icon, by "//" just keep the code so its a copy and paste on other .kt files
                Toast.makeText(context, "Home clicked", Toast.LENGTH_SHORT).show()
            }) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = Color.White
                )
            }
        }
    }
}

/*PREVIEW */
@Preview(showSystemUi = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen()
}
