package com.example.kleenpride.admin.ui.detailers

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kleenpride.admin.ui.overview.AdminTopBar
import com.example.kleenpride.ui.theme.LimeGreen
import java.text.SimpleDateFormat
import java.util.*

class AdminDetailersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AdminDetailersScreen() }
    }
}

// Data class for detailers
data class Detailer(
    val name: String,
    val email: String,
    val phone: String,
    val rating: Float = 0f,
    val totalJobs: Int = 0,
    val earnings: Int = 0,
    val status: String = "ACTIVE",
    val statusColor: Color = LimeGreen,
    val joinDate: String = SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(Date())
)

@Composable
fun AdminDetailersScreen() {
    var showAddDialog by remember { mutableStateOf(false) }

    var detailers by remember {
        mutableStateOf(
            listOf(
                Detailer(
                    name = "James Smith",
                    email = "James.smith@kleenpride.com",
                    phone = "+27 82 123 4567",
                    rating = 4.8f,
                    totalJobs = 36,
                    earnings = 8905,
                    joinDate = "Jan 2024"
                ),
                Detailer(
                    name = "Ja Rule",
                    email = "Ja.rule@kleenpride.com",
                    phone = "+27 83 234 5678",
                    rating = 4.6f,
                    totalJobs = 28,
                    earnings = 6850,
                    joinDate = "Feb 2024"
                ),
                Detailer(
                    name = "Cornell Haynes",
                    email = "Cornell.haynes@kleenpride.com",
                    phone = "+27 84 345 6789",
                    rating = 4.9f,
                    totalJobs = 46,
                    earnings = 12500,
                    joinDate = "Dec 2023"
                )
            )
        )
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = { AdminTopBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(Color.Black)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            DetailersHeader(
                total = detailers.size,
                onAddClick = { showAddDialog = true }
            )

            Spacer(Modifier.height(12.dp))

            detailers.forEachIndexed { index, detailer ->
                DetailerCard(
                    detailer = detailer,
                    onToggleStatus = {
                        val updatedDetailers = detailers.toMutableList()
                        val isActive = detailer.status == "ACTIVE"
                        updatedDetailers[index] = detailer.copy(
                            status = if (isActive) "INACTIVE" else "ACTIVE",
                            statusColor = if (isActive) Color.Red else LimeGreen
                        )
                        detailers = updatedDetailers
                    },
                    onDelete = {
                        val updatedDetailers = detailers.toMutableList()
                        updatedDetailers.removeAt(index)
                        detailers = updatedDetailers
                    }
                )
            }

            Spacer(Modifier.height(80.dp))
        }

        if (showAddDialog) {
            AddDetailerDialog(
                onDismiss = { showAddDialog = false },
                onSave = { name, email, phone ->
                    val newDetailer = Detailer(name = name, email = email, phone = phone)
                    detailers = detailers + newDetailer
                    showAddDialog = false
                }
            )
        }
    }
}

@Composable
fun DetailersHeader(total: Int, onAddClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                "All Detailers",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "$total registered",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(containerColor = LimeGreen),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("+ Add Detailer", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DetailerCard(
    detailer: Detailer,
    onToggleStatus: () -> Unit,
    onDelete: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .background(Color(0xFF0F0F0F), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(LimeGreen, RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        detailer.name.first().toString(),
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            detailer.name,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.width(8.dp))
                        StatusChip(text = detailer.status, color = detailer.statusColor)
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(detailer.email, color = Color.Gray, fontSize = 13.sp)
                    Text(detailer.phone, color = Color.Gray, fontSize = 13.sp)
                }
            }

            Box {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { menuExpanded = true }
                )
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(if (detailer.status == "ACTIVE") "Deactivate" else "Activate") },
                        onClick = {
                            onToggleStatus()
                            menuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete") },
                        onClick = {
                            onDelete()
                            menuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DetailerStatBox(
                label = "Rating",
                value = detailer.rating.toString(),
                valueColor = Color.White,
                icon = Icons.Default.Star
            )

            DetailerStatBox(
                label = "Jobs",
                value = detailer.totalJobs.toString(),
                valueColor = Color.White,
                icon = null
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF0A0A0A), RoundedCornerShape(8.dp))
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Earnings", color = Color.Gray, fontSize = 11.sp)
                Spacer(Modifier.height(4.dp))
                Text(
                    "R${String.format("%,d", detailer.earnings)}",
                    color = LimeGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RowScope.DetailerStatBox(
    label: String,
    value: String,
    valueColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector?
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .background(Color(0xFF0A0A0A), RoundedCornerShape(8.dp))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(icon, contentDescription = null, tint = LimeGreen, modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
            }
            Text(label, color = Color.Gray, fontSize = 11.sp)
        }
        Spacer(Modifier.height(4.dp))
        Text(value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StatusChip(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.2f), shape = RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AddDetailerDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF111111),
        title = { Text("Add New Detailer", color = Color.White, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name", color = Color.Gray) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1A1A1A),
                        unfocusedContainerColor = Color(0xFF1A1A1A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = LimeGreen
                    )
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address", color = Color.Gray) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1A1A1A),
                        unfocusedContainerColor = Color(0xFF1A1A1A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = LimeGreen
                    )
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number", color = Color.Gray) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1A1A1A),
                        unfocusedContainerColor = Color(0xFF1A1A1A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = LimeGreen
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && email.isNotBlank() && phone.isNotBlank()) {
                        onSave(name, email, phone)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = LimeGreen)
            ) { Text("Save", color = Color.Black, fontWeight = FontWeight.Bold) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel", color = Color.Gray) } }
    )
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
fun PreviewAdminDetailersScreen() {
    MaterialTheme { AdminDetailersScreen() }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
fun PreviewDetailerCard() {
    MaterialTheme {
        DetailerCard(
            detailer = Detailer(
                name = "Ja Rule",
                email = "Ja.rule@kleenpride.com",
                phone = "+27 82 123 4567",
                rating = 4.8f,
                totalJobs = 46,
                earnings = 18000,
                status = "ACTIVE",
                statusColor = LimeGreen,
                joinDate = "Jan 2024"
            ),
            onToggleStatus = {},
            onDelete = {}
        )
    }
}