package com.example.kleenpride.admin.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.kleenpride.admin.ui.users.ManageUsersActivity

class ProfileScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AdminProfileScreen(
                adminName = "Andrew Haynes",
                email = "AndrewH@kleenpride.com",
                phone = "+27 82 262 6130",
                memberSince = "Jan 2024",

                onManageUsers = {
                    startActivity(Intent(this, ManageUsersActivity::class.java))
                },


                onEditProfile = { },
                onLogout = { },

                onBack = { finish() }
            )
        }
    }
}