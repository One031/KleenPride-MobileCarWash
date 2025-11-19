package com.example.kleenpride

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kleenpride.admin.ui.overview.AdminOverviewActivity
import com.example.kleenpride.detailers.ui.homescreen.DetailerMainActivity
import com.example.kleenpride.ui.homescreen.MainActivity // Your customer main activity
import com.example.kleenpride.ui.theme.LimeGreen
import com.example.kleenpride.viewmodel.UserDataViewModel

/**
 * RouterActivity - Routes users based on their role after authentication
 * This activity is called AFTER login/signup to determine where to send the user
 * Based on role: CUSTOMER → MainActivity, DETAILER → DetailerApp, ADMIN → AdminDashboard
 */
class RouterActivity : ComponentActivity() {

    private val userDataViewModel: UserDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show loading screen while we fetch user data
        setContent {
            LoadingScreen()
        }

        // Observe user data and route based on role
        userDataViewModel.userData.observe(this) { userData ->
            when (userData.role) {
                "CUSTOMER" -> navigateToCustomerApp()
                "DETAILER" -> navigateToDetailerApp()
                "ADMIN" -> navigateToAdminDashboard()
                else -> {
                    // Default to customer if role is missing or unknown
                    navigateToCustomerApp()
                }
            }
        }

        // Handle errors - if we can't load user data, go to customer app as fallback
        userDataViewModel.error.observe(this) { error ->
            if (error != null) {
                navigateToCustomerApp()
            }
        }
    }

    private fun navigateToCustomerApp() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun navigateToDetailerApp() {
        startActivity(Intent(this, DetailerMainActivity::class.java))
        finish()
    }

    private fun navigateToAdminDashboard() {
        startActivity(Intent(this, AdminOverviewActivity::class.java))
        finish()
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                color = LimeGreen,
                modifier = Modifier.size(48.dp)
            )
            Text(
                text = "Loading your profile...",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }
}