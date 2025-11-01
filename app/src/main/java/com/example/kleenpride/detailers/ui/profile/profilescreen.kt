package com.example.kleenpride.detailers.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.kleenpride.detailers.ui.components.BottomNavBar

@Composable
fun ProfileScreen() {
    val darkBackground = Color(0xFF0A0A0A)
    val cardBackground = Color(0xFF141414)
    val neonGreen = Color(0xFF00FF66)
    val softGray = Color(0xFFB0B0B0)
    val dangerRed = Color(0xFFFF3B30)

    var selectedItem by remember { mutableStateOf("Profile") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground)
    ) {
        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("My Profile", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(neonGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("L", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Profile Info
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(neonGreen, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("L", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 32.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Leo Lennards", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = neonGreen, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("4.8 (127 jobs)", color = neonGreen, fontSize = 14.sp)
                }
                Text("Member since Jan 2024", color = softGray, fontSize = 13.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Specialties
            ProfileSection(title = "Specialties") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SpecialtyTag("Premium Detail", neonGreen)
                    SpecialtyTag("Paint Correction", neonGreen)
                    SpecialtyTag("Ceramic Coating", neonGreen)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Email
            ProfileSection(title = "Email") {
                ProfileItem(Icons.Default.Email, "leo.lennards@kleenpride.com", cardBackground)
            }

            // Phone
            ProfileSection(title = "Phone") {
                ProfileItem(Icons.Default.Phone, "+27 82 262 6130", cardBackground)
            }

            // Availability
            ProfileSection(title = "Availability") {
                ProfileItem(Icons.Default.Schedule, "Mon–Fri, 8AM–6PM", cardBackground)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Buttons
            ProfileAction(Icons.Default.Edit, "Edit Profile", neonGreen)
            ProfileAction(Icons.Default.Settings, "Settings", neonGreen)
            ProfileAction(Icons.Default.Logout, "Log Out", dangerRed)

            Spacer(modifier = Modifier.height(70.dp)) // Prevent overlap with bottom nav
        }

        // Bottom NavBar
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavBar(
                selectedItem = selectedItem,
                onItemSelected = { selectedItem = it }
            )
        }
    }
}

@Composable
fun ProfileSection(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF141414), RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
fun SpecialtyTag(label: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(label, color = color, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ProfileItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, background: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(background, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF00FF66), modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, color = Color.White, fontSize = 14.sp)
    }
}

@Composable
fun ProfileAction(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color(0xFF141414), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Text(text, color = color, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}
