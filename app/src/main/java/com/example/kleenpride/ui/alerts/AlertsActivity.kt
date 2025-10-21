package com.example.kleenpride.ui.alerts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.example.kleenpride.ui.components.BottomNavBar

class AlertsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Scaffold(
                bottomBar = { BottomNavBar(currentScreen = "alerts") }
            ) { innerPadding: PaddingValues ->
                Text(
                    text = "Alerts Page",
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}