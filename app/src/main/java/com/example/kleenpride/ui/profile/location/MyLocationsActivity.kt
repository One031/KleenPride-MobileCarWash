package com.example.kleenpride.ui.profile.location

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.kleenpride.ui.theme.KleenPrideTheme

class MyLocationsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KleenPrideTheme {
                MyLocationsScreen()
            }
        }
    }
}
