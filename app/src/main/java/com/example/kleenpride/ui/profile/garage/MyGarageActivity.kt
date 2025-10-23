package com.example.kleenpride.ui.profile.garage

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.kleenpride.ui.theme.KleenPrideTheme

class MyGarageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KleenPrideTheme {
                MyGarageScreen()
            }
        }
    }
}
