package com.example.kleenpride.ui.auth.setup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.kleenpride.ui.theme.KleenPrideTheme

/**
 * AccountSetupActivity hosts all onboarding-related composables (Welcome, Step 1, Step 2, etc.) and acts as the entry point when the app is launched for the first time.
 *
 */
class AccountSetupActivity : ComponentActivity()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KleenPrideTheme {
                val navController = rememberNavController()
                AccountSetupNavHost(navController = navController)
            }
        }

    }
}