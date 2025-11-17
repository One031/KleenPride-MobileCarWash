package com.example.kleenpride.detailers.ui.booking

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kleenpride.ui.theme.LimeGreen

@Composable
fun DetailerBookingScreen(
    customerName: String,
    service: String,
    car: String,
    scheduledTime: String,
    address: String,
    price: String,
    onBack: () -> Unit = {},
    onOpenMaps: () -> Unit = {},
    onCall: () -> Unit = {},
    onStartJob: () -> Unit = {},
    onCompleteJob: () -> Unit = {}
) {
    val primary = LimeGreen
    val surface = Color(0xFF111111)
    val onSurface = Color.White
    val background = Color.Black
    var jobStarted by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Surface(color = background, tonalElevation = 0.dp, shadowElevation = 0.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = onSurface)
                    }
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("Booking Details", color = onSurface, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        Text(customerName, color = onSurface.copy(alpha = 0.7f), fontSize = 12.sp)
                    }
                }
            }
        },
        containerColor = background,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Map placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(surface),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize().padding(16.dp)) {
                        Icon(Icons.Default.Place, contentDescription = "Map", tint = primary, modifier = Modifier.size(56.dp))
                        Spacer(Modifier.height(8.dp))
                        Text("Map preview (Google Maps integration pending)", color = onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
                        Spacer(Modifier.weight(1f))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(primary),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.padding(start = 16.dp)) {
                                Text("Navigate to customer", color = Color(0xFF0F1B12).copy(alpha = 0.85f), fontSize = 12.sp)
                                Text("Tap to open Maps", color = Color(0xFF0F1B12), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            IconButton(onClick = onOpenMaps, modifier = Modifier.padding(end = 8.dp)) {
                                Icon(Icons.Default.Map, contentDescription = "Open maps", tint = Color(0xFF0F1B12))
                            }
                        }
                    }
                }

                // Customer card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(customerName.take(1).uppercase(), color = primary, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(customerName, color = onSurface, fontWeight = FontWeight.SemiBold)
                            Text(service, color = onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(address, color = onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
                        }
                        IconButton(onClick = onCall, modifier = Modifier.size(44.dp).background(primary, shape = CircleShape)) {
                            Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.Black)
                        }
                    }
                }

                // Booking details card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Vehicle", color = onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
                                Text(car, color = onSurface, fontWeight = FontWeight.Medium)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Schedule", color = onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
                                Text(scheduledTime, color = onSurface, fontWeight = FontWeight.Medium)
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text("Price", color = onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
                                Text(price, color = primary, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = onOpenMaps,
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = LimeGreen
                                )
                            ) {
                                Icon(Icons.Default.Map, contentDescription = null, tint = LimeGreen)
                                Spacer(Modifier.width(8.dp))
                                Text("Open in Maps", color = LimeGreen)
                            }
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                // Bottom actions
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Accept / Start / Complete
                    when {
                        !jobStarted -> {
                            Button(
                                onClick = {
                                    jobStarted = true
                                    onStartJob()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = primary),
                                modifier = Modifier.fillMaxWidth().height(52.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Start Job", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                        }
                        else -> {
                            Button(
                                onClick = onCompleteJob,
                                colors = ButtonDefaults.buttonColors(containerColor = primary),
                                modifier = Modifier.fillMaxWidth().height(52.dp),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Mark Complete", color = Color.Black, fontWeight = FontWeight.Bold)
                            }
                            OutlinedButton(
                                onClick = onOpenMaps,
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = onSurface)
                            ) {
                                Text("Open Navigation")
                            }
                        }
                    }

                    // Quick actions row
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .border(BorderStroke(2.dp, Brush.horizontalGradient(listOf(primary, primary.copy(alpha = 0.9f)))), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("ETA: 12 min", color = onSurface.copy(alpha = 0.9f))
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .border(BorderStroke(2.dp, Brush.horizontalGradient(listOf(primary, primary.copy(alpha = 0.9f)))), RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Distance: 4.2 km", color = onSurface.copy(alpha = 0.9f))
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DetailerBookingScreenPreview() {
    DetailerBookingScreen(
        customerName = "Raphael Saadiq",
        service = "Ultimate Detail",
        car = "Honda Civic 1996",
        scheduledTime = "9:00 AM - 6-8 hrs",
        address = "789 Pine Road, Riverside",
        price = "R249"
    )
}
