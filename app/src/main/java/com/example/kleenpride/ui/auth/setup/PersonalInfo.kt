package com.example.kleenpride.ui.auth.setup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import com.example.kleenpride.ui.components.CustomButton
import com.example.kleenpride.ui.components.CustomTextField
import com.example.kleenpride.viewmodel.AccountSetupViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * Step 2: Fill in Personal Information
 */

@Composable
fun PersonalInfo(navController: NavController, accountViewModel: AccountSetupViewModel = viewModel()){

    var message by remember { mutableStateOf<String?>(null) }

    Surface (color = Color.Black, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // Progress
            StepProgressBar(currentStep = 2)
            Spacer(Modifier.height(32.dp))

            // Header
            Text(
                text = "Personal Information",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(20.dp))

            // Fields
            CustomTextField(accountViewModel.preferredName.value, { accountViewModel.preferredName.value = it }, "Preferred Name")
            Spacer(Modifier.height(10.dp))
            CustomTextField(accountViewModel.phoneNumber.value, { accountViewModel.phoneNumber.value = it }, "Phone Number")
            Spacer(Modifier.height(10.dp))
            CustomTextField(accountViewModel.address.value, { accountViewModel.address.value = it }, "Address")
            Spacer(Modifier.height(20.dp))

            // Navigation Buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                CustomButton(
                    text = "Back",
                    onClick = { navController.popBackStack() },
                    containerColor = Color.Gray,
                    contentColor = Color.Black,
                    modifier = Modifier.weight(1f),
                )
                Spacer(Modifier.width(16.dp))

                CustomButton(
                    text = "Next",
                    onClick = {
                        if ( accountViewModel.preferredName.value.isNotEmpty() &&
                            accountViewModel.phoneNumber.value.isNotEmpty() &&
                            accountViewModel.address.value.isNotEmpty()){
                            accountViewModel.savePersonalInfo()
                            navController.navigate("car_details")
                        }else {
                            message = "Please fill in all fields"
                        }
                    },
                    containerColor = Color.Green,
                    contentColor = Color.Black,
                    modifier = Modifier.weight(1f),
                )
            }

            // Error message
            message?.let { Text(it, color = Color.Red, modifier = Modifier.padding(top = 10.dp)) }
        }
    }
}