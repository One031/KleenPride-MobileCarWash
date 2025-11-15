package com.example.kleenpride.ui.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import com.example.kleenpride.ui.components.BottomNavBar
import com.example.kleenpride.ui.theme.KleenPrideTheme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KleenPrideTheme {
                Scaffold(
                    bottomBar = { BottomNavBar(currentScreen = "profile") }
                ) { innerPadding: PaddingValues ->
                    // Pass the padding from Scaffold to ProfileScreen
                    ProfileScreen()
                }
            }
        }
    }
}
