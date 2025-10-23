package com.example.kleenpride.ui.profile.accinfo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.kleenpride.ui.theme.KleenPrideTheme

class AccountDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KleenPrideTheme {
                // This calls your existing composable screen
                AccountDetailsScreen()
            }
        }
    }
}
