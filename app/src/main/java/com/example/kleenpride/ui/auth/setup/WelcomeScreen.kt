package com.example.kleenpride.ui.auth.setup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.kleenpride.R

/**
 * This composable represent the very first screen a user sees when launching the app
 * It gives them two clear options:
 * Start the onboarding process
 * Go to the login screen
 */

@Composable
fun WelcomeScreen(
    navController: NavController,
    onGetStartedClick: () -> Unit,
    onLoginClick: () -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ){
    // Box allows stacking elements (like layering items if needed)
    Box(
        modifier = Modifier
            .fillMaxSize() // Takes up the entire screen
            .padding(24.dp) // Adds padding around the content
    ) {
        // Main layout column containing all UI elements
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center) // Center the column vertically and horizontally
                .padding(horizontal = 16.dp), // Additional side padding for breathing room
            horizontalAlignment = Alignment.CenterHorizontally, // Center all child elements horizontally
            verticalArrangement = Arrangement.SpaceEvenly // Evenly space items vertically
        ) {
            /*
            This is where the branding image or car-wash icon goes.
             */
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = 16.dp)
            )

            /**
             * App name
             * Bold and large text that immediately introduces the app
             */

            Text(
                text = "Welcome to KleenPride",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Green
                ),
                textAlign = TextAlign.Center
            )

            /**
             * Short Description
             * Explains the app's purpose
             */
            Text(
                text = "Book washes, car detailing, and enjoy a premium experience - anytime, anywhere.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            /**
             * Button Section
             * Contains two buttons stacked vertically
             */
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                /**
                 * "Let's get started" Button"
                 */
                Button(
                    onClick = onGetStartedClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "Let's Get Started",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                /**
                 * "Login" Button
                 * A secondary button for users who already have an account.
                 */

                Button(
                    onClick = onLoginClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    border = ButtonDefaults.outlinedButtonBorder
                ) {
                    Text(
                        text = "Login",
                        color = Color.Black,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
        }
    }

}
@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    WelcomeScreen(navController = rememberNavController(),
        onGetStartedClick = {},
        onLoginClick = {}
    )
}