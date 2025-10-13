package com.example.kleenpride.ui.auth.setup

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kleenpride.R
import com.example.kleenpride.ui.auth.LoginActivity
import kotlinx.coroutines.delay

@Composable
fun ConfirmationScreen(navController: NavController) {

    // Auto-redirect after 2 seconds
    LaunchedEffect(Unit) {
        delay(3000)
        navController.context.startActivity(
            Intent(navController.context, LoginActivity::class.java)
        )
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ){
            // Success Icon
            Image(
                painter = painterResource(id = R.drawable.ic_success),
                contentDescription = "Success Icon",
                modifier = Modifier
                    .size(140.dp)
                    .padding(bottom = 24.dp)
            )
            // Headline
            Text(
                text = "Account Created",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Subtext
            Text(
                text = "Welcome to KleenPride - your car care experience starts now!",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(32.dp))

            // Manual Button fallback
            Button(
                onClick = {  navController.context.startActivity(
                    Intent(navController.context, LoginActivity::class.java)
                ) },
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth(0.7f)
            ){
                Text(text = "Login", fontSize = 18.sp)
            }
        }
    }
}