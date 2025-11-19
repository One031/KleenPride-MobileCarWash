package com.example.kleenpride.admin.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kleenpride.ui.theme.LimeGreen

@Composable
fun AdminProfileScreen(
    adminName: String,
    email: String,
    phone: String,
    memberSince: String,
    onManageUsers: () -> Unit = {},
    onEditProfile: () -> Unit = {},
    onLogout: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val background = Color.Black
    val surface = Color(0xFF111111)
    val onSurface = Color.White
    val primary = LimeGreen

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // 🔙 Back Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onBack() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            Spacer(modifier = Modifier.width(6.dp))
            Text("Back", color = Color.White, fontSize = 16.sp)
        }

        // Header
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = adminName.take(1).uppercase(),
                    color = Color.Black,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(adminName, color = onSurface, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(
                "System Administrator",
                color = primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text("Member since $memberSince", color = onSurface.copy(alpha = 0.6f), fontSize = 12.sp)
        }

        // Contact Info
        Card(
            colors = CardDefaults.cardColors(containerColor = surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Email, contentDescription = null, tint = primary)
                    Spacer(Modifier.width(12.dp))
                    Text(email, color = onSurface)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Phone, contentDescription = null, tint = primary)
                    Spacer(Modifier.width(12.dp))
                    Text(phone, color = onSurface)
                }
            }
        }

        // Admin tools (ONLY Manage Users now)
        Card(
            colors = CardDefaults.cardColors(containerColor = surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(4.dp)) {
                AdminActionItem("Manage Users", Icons.Default.Group, onManageUsers)
            }
        }

        // Edit profile
        Card(
            colors = CardDefaults.cardColors(containerColor = surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onEditProfile() }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = primary)
                Spacer(Modifier.width(12.dp))
                Text("Edit Profile", color = onSurface)
            }
        }

        // Logout
        Card(
            colors = CardDefaults.cardColors(containerColor = surface),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onLogout() }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Logout, contentDescription = null, tint = Color.Red)
                Spacer(Modifier.width(12.dp))
                Text("Log Out", color = Color.Red)
            }
        }
    }
}

@Composable
private fun AdminActionItem(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = LimeGreen)
        Spacer(Modifier.width(12.dp))
        Text(label, color = Color.White, fontSize = 15.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun AdminProfilePreview() {
    AdminProfileScreen(
        adminName = "Andrew Haynes",
        email = "AndrewH@kleenpride.com",
        phone = "+27 82 262 6130",
        memberSince = "Jan 2024"
    )
}
