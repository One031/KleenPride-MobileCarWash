package com.example.kleenpride.admin.ui.users

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.kleenpride.ui.theme.LimeGreen

data class AdminUser(
    val name: String,
    val email: String,
    val role: String,
    val isActive: Boolean
)

@Composable
fun ManageUsersScreen(
    users: List<AdminUser>,
    onBack: () -> Unit = {},
    onUserClick: (AdminUser) -> Unit = {},
    onEditUser: (AdminUser) -> Unit = {},
    onDeleteUser: (AdminUser) -> Unit = {},
    onToggleActive: (AdminUser) -> Unit = {}
) {
    var search by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        // Top bar
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            Spacer(Modifier.width(8.dp))
            Text("Manage Users", fontSize = 20.sp, color = Color.White)
        }

        Spacer(Modifier.height(16.dp))

        // Search bar
        TextField(
            value = search,
            onValueChange = { search = it },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.LightGray)
            },
            placeholder = { Text("Search users...", color = Color.Gray) },
            singleLine = true,
            colors = TextFieldDefaults.colors( // <-- updated to Material3 API
                focusedContainerColor = Color(0xFF111111),
                unfocusedContainerColor = Color(0xFF111111),
                disabledContainerColor = Color(0xFF111111),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                disabledTextColor = Color.Gray,
                cursorColor = LimeGreen,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        // Filtered list
        val filtered = users.filter {
            it.name.contains(search, ignoreCase = true) ||
                    it.role.contains(search, ignoreCase = true)
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(filtered) { user ->
                UserCard(
                    user = user,
                    onClick = { onUserClick(user) },
                    onEdit = { onEditUser(user) },
                    onDelete = { onDeleteUser(user) },
                    onToggleActive = { onToggleActive(user) }
                )
            }
        }
    }
}

@Composable
private fun UserCard(
    user: AdminUser,
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onToggleActive: () -> Unit
) {
    var menuOpen by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111111)),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(Modifier.padding(16.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(LimeGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        user.name.take(1).uppercase(),
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(user.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(user.email, color = Color.Gray, fontSize = 13.sp)
                    Text(
                        user.role,
                        color = LimeGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Action menu
                Box {
                    IconButton(onClick = { menuOpen = true }) {
                        Icon(Icons.Default.MoreVert, null, tint = Color.White)
                    }

                    DropdownMenu(
                        expanded = menuOpen,
                        onDismissRequest = { menuOpen = false },
                        containerColor = Color(0xFF222222)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit User", color = Color.White) },
                            onClick = { menuOpen = false; onEdit() }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    if (user.isActive) "Suspend User" else "Restore User",
                                    color = Color.White
                                )
                            },
                            onClick = { menuOpen = false; onToggleActive() }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete User", color = Color.Red) },
                            onClick = { menuOpen = false; onDelete() }
                        )
                    }
                }
            }

            // Status pill
            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (user.isActive) LimeGreen.copy(alpha = 0.2f) else Color.Red.copy(alpha = 0.2f))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Text(
                    text = if (user.isActive) "ACTIVE" else "SUSPENDED",
                    color = if (user.isActive) LimeGreen else Color.Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ManageUsersPreview() {
    val sampleUsers = listOf(
        AdminUser("Leo Lennards", "leo@kleenpride.com", "Admin", true),
        AdminUser("Marcus Thompson", "marcus@kleenpride.com", "Detailer", true),
        AdminUser("Jessica Brown", "jessica@kleenpride.com", "Customer", false),
        AdminUser("Mike Chen", "mike@kleenpride.com", "Customer", true)
    )

    ManageUsersScreen(users = sampleUsers)
}