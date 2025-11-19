package com.example.kleenpride.admin.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.kleenpride.ui.theme.LimeGreen

@Composable
fun AdminProfileScreen(
    adminName: String,
    email: String,
    phone: String,
    memberSince: String,
    onManageUsers: () -> Unit = {},
    onSaveProfile: (String, String, String, String, String) -> Unit = { _, _, _, _, _ -> },
    onLogout: () -> Unit = {},
    onBack: () -> Unit = {}
) {
    val background = Color.Black
    val surface = Color(0xFF111111)
    val onSurface = Color.White
    val primary = LimeGreen

    // --------------------------
    // POPUP DIALOG STATE
    // --------------------------
    var showEditDialog by remember { mutableStateOf(false) }

    // --------------------------
    // UI CONTENT
    // --------------------------
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
            Text("System Administrator", color = primary, fontSize = 14.sp)
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

        // Manage Users
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
                .clickable { showEditDialog = true }
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

    // --------------------------
    // EDIT PROFILE POPUP DIALOG
    // --------------------------
    if (showEditDialog) {
        EditProfileDialog(
            initialFirst = adminName.split(" ").firstOrNull() ?: "",
            initialLast = adminName.split(" ").drop(1).joinToString(" "),
            initialEmail = email,
            initialPhone = phone,
            initialPassword = "",
            onDismiss = { showEditDialog = false },
            onSave = { first, last, newEmail, newPhone, newPassword ->
                onSaveProfile(first, last, newEmail, newPhone, newPassword)
                showEditDialog = false
            }
        )
    }
}

@Composable
fun EditProfileDialog(
    initialFirst: String,
    initialLast: String,
    initialEmail: String,
    initialPhone: String,
    initialPassword: String,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String) -> Unit
) {
    var firstName by remember { mutableStateOf(initialFirst) }
    var lastName by remember { mutableStateOf(initialLast) }
    var email by remember { mutableStateOf(initialEmail) }
    var phone by remember { mutableStateOf(initialPhone) }
    var password by remember { mutableStateOf(initialPassword) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF222222),
        title = {
            Text("Edit Profile", color = Color.White, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                EditField("First Name", firstName) { firstName = it }
                EditField("Last Name", lastName) { lastName = it }
                EditField("Email", email) { email = it }
                EditField("Phone Number", phone) { phone = it }
                EditField("Password", password, isPassword = true) { password = it }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onSave(firstName, lastName, email, phone, password)
            }) {
                Text("Save", color = LimeGreen)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Red)
            }
        }
    )
}

@Composable
fun EditField(
    label: String,
    value: String,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.White) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = LimeGreen,
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = LimeGreen,
            unfocusedLabelColor = Color.Gray,
            cursorColor = LimeGreen,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth()
    )
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