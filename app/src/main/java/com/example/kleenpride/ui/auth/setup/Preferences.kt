package com.example.kleenpride.ui.auth.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kleenpride.ui.components.CustomButton
import com.example.kleenpride.viewmodel.AccountSetupViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kleenpride.ui.theme.LimeGreen

@Composable
fun Preferences (navController: NavController, accountViewModel: AccountSetupViewModel = viewModel()) {

    var message by remember { mutableStateOf<String?>(null) }

    Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress
            StepProgressBar(currentStep = 4)
            Spacer(Modifier.height(32.dp))

            // Header
            Text(
                text = "Preferences",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(20.dp))

            // Toggles
            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Receive Reminders", color = Color.White)
                    Switch(
                        checked = accountViewModel.receiveReminders.value,
                        onCheckedChange = { accountViewModel.receiveReminders.value = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Green, //made it like the profile screen toggles
                            checkedTrackColor = Color.Green.copy(alpha = 0.4f),
                            uncheckedThumbColor = Color.Gray
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Receive Promotions", color = Color.White)
                    Switch(
                        checked = accountViewModel.receivePromotions.value,
                        onCheckedChange = { accountViewModel.receivePromotions.value = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Green,
                            checkedTrackColor = Color.Green.copy(alpha = 0.4f),
                            uncheckedThumbColor = Color.Gray
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Enable Notifications", color = Color.White)
                    Switch(
                        checked = accountViewModel.enableNotifications.value,
                        onCheckedChange = { accountViewModel.enableNotifications.value = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Green,
                            checkedTrackColor = Color.Green.copy(alpha = 0.4f),
                            uncheckedThumbColor = Color.Gray
                        )
                    )
                }
            }
            Spacer(Modifier.height(30.dp))

            // Navigation Buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                CustomButton(
                    text = "Back",
                    onClick = { navController.popBackStack() },
                    containerColor = Color.Gray,
                    contentColor = Color.Black,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(16.dp))
                CustomButton(
                    text = "Create Account",
                    onClick = {
                        // Save account setup
                        accountViewModel.savePreferences {
                            navController.navigate("confirmation"){
                                popUpTo("welcome") { inclusive = true }
                            }
                        }
                    },
                    containerColor = Color.Green,
                    contentColor = Color.Black,
                    modifier = Modifier.weight(1f)
                )
            }

            message?.let { Text(it, color = Color.Red, modifier = Modifier.padding(top = 10.dp)) }
        }
    }
}
