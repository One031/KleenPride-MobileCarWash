package com.example.kleenpride.ui.booking

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
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun TrackBookingScreen(
    onBack: () -> Unit = {},
    onCall: () -> Unit = {},
    onContact: () -> Unit = {},
    onCancel: () -> Unit = {}
) {
    // Use app-specific colors so preview/runtime match BookingScreen theme
    val primary = LimeGreen
    val surface = Color(0xFF111111)
    val onSurface = Color.White
    val background = Color.Black

    Scaffold(
        topBar = {
            Surface(
                color = background,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = onSurface)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text("Track Booking", style = MaterialTheme.typography.titleMedium, color = onSurface)
                        Text(
                            "Booking ID: B001",
                            style = MaterialTheme.typography.labelSmall,
                            color = onSurface.copy(alpha = 0.7f)
                        )
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
                // Map Header + ETA pill
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(surface),
                    contentAlignment = Alignment.TopCenter
                ) {
                    // Map placeholder
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = "Map",
                            tint = primary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Google Maps Integration", color = onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        // ETA pill
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(primary)
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    "Estimated Time of Arrival",
                                    color = Color(0xFF0F1B12).copy(alpha = 0.8f),
                                    fontSize = 12.sp
                                )
                                Text("21 mins", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F1B12))
                            }
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = "Navigate",
                                tint = Color(0xFF0F1B12),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }

                // Detailer Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(primary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("L", color = primary, fontWeight = FontWeight.Bold)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Tevin Campbell", color = onSurface, fontWeight = FontWeight.SemiBold)
                            Text("Your detailer", color = onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Card(
                                    modifier = Modifier.weight(1f),
                                    colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.06f)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                        Text("⭐", fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text("Rating", color = onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
                                            Text("4.8", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                                Card(
                                    modifier = Modifier.weight(1f),
                                    colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.06f)),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text("Service", color = onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
                                        Text("Pride Wash", fontWeight = FontWeight.Bold, color = primary)
                                    }
                                }
                            }
                        }
                        IconButton(
                            onClick = onCall,
                            modifier = Modifier
                                .size(44.dp)
                                .background(primary, shape = CircleShape)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = "Call", tint = Color.Black)
                        }
                    }
                }

                // Booking Details
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Booking Details", fontWeight = FontWeight.SemiBold, color = onSurface)
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Vehicle", color = onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
                                Text("Hyundai i20", fontWeight = FontWeight.Medium, color = onSurface)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Scheduled Time", color = onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
                                Text("2:00 PM", fontWeight = FontWeight.Medium, color = onSurface)
                            }
                        }
                        Column {
                            Text("Service Location", color = onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
                            Text("21 Long Street, Cape Town", fontWeight = FontWeight.Medium, color = onSurface)
                        }
                    }
                }

                // Service Progress (timeline)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f, fill = false),
                    colors = CardDefaults.cardColors(containerColor = surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Text("Service Progress", fontWeight = FontWeight.SemiBold, color = onSurface)
                        TimelineItem(title = "Booking Confirmed", time = "1:30 PM", done = true, primary = primary, onSurface = onSurface)
                        TimelineItem(title = "Detailer En Route", time = "1:45 PM", done = true, primary = primary, onSurface = onSurface)
                        TimelineItem(title = "Service In Progress", time = "2:00 PM", done = false, primary = primary, onSurface = onSurface)
                        TimelineItem(title = "Service Completed", time = "Est. 2:45 PM", done = false, completedStyle = false, primary = primary, onSurface = onSurface)
                    }
                }

                // Progress bar + total + buttons
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Service Progress", color = onSurface.copy(alpha = 0.9f))
                        Text("60%", color = primary, fontWeight = FontWeight.SemiBold)
                    }
                    LinearProgressIndicator(
                        progress = 0.6f,
                        color = primary,
                        trackColor = onSurface.copy(alpha = 0.12f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(BorderStroke(2.dp, Brush.horizontalGradient(listOf(primary, primary.copy(alpha = 0.9f)))), RoundedCornerShape(12.dp))
                            .padding(16.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Total Amount", color = onSurface.copy(alpha = 0.7f))
                                Text("R250", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primary)
                            }
                            Text("Payment will be processed after service", color = onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
                        }
                    }

                    Button(
                        onClick = onContact,
                        colors = ButtonDefaults.buttonColors(containerColor = primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Contact Detailer", color = Color.Black)
                    }

                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = onSurface)
                    ) {
                        Text("Cancel Booking")
                    }
                }
            }
        }
    )
}

@Composable
private fun TimelineItem(
    title: String,
    time: String,
    done: Boolean,
    completedStyle: Boolean = true,
    primary: Color,
    onSurface: Color
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(48.dp)) {
            val circleColor = if (done) primary else onSurface.copy(alpha = 0.3f)
            Surface(
                shape = CircleShape,
                color = circleColor,
                tonalElevation = 0.dp,
                modifier = Modifier.size(28.dp)
            ) {}
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(40.dp)
                    .background(if (done && completedStyle) primary else onSurface.copy(alpha = 0.12f))
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, fontWeight = FontWeight.SemiBold, color = onSurface)
            Spacer(modifier = Modifier.height(4.dp))
            Text(time, color = onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TrackBookingScreenPreview() {
    TrackBookingScreen()
}
