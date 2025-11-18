package com.example.kleenpride.admin.ui.users

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

class ManageUsersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val users = listOf(
            AdminUser("Leo Lennards", "leo@kleenpride.com", "Admin", true),
            AdminUser("Marcus Thompson", "marcus@kleenpride.com", "Detailer", true),
            AdminUser("Jessica Brown", "jessica@kleenpride.com", "Customer", false),
            AdminUser("Mike Chen", "mike@kleenpride.com", "Customer", true)
        )

        setContent {
            ManageUsersScreen(
                users = users,
                onBack = { finish() }, // closes this activity
                onUserClick = { /* optional */ },
                onEditUser = { /* optional */ },
                onDeleteUser = { /* optional */ },
                onToggleActive = { /* optional */ }
            )
        }
    }
}