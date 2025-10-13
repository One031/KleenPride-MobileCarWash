package com.example.kleenpride.ui.auth.setup

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.kleenpride.ui.auth.LoginActivity

/**
 * This composable sets up the navigation graph for all onboarding-related screens
 */

@Composable
fun AccountSetupNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(
                navController,
                onGetStartedClick = { navController.navigate("basic_info") },
                onLoginClick = {
                    navController.context.startActivity(
                        Intent(navController.context, LoginActivity::class.java)
                    )
                })
        }
        composable("basic_info") { BasicInfo(navController) }
        composable ("personal_info") {PersonalInfo(navController)}
        composable ("car_details") {CarDetails(navController)}
        composable("preferences") { Preferences(navController) }
        composable("confirmation") { ConfirmationScreen(navController) }
    }

}