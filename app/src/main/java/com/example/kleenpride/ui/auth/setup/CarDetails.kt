package com.example.kleenpride.ui.auth.setup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.kleenpride.ui.components.CustomTextField

@Composable
fun CarDetails(navController: NavController, accountViewModel: AccountSetupViewModel = viewModel()){

    // Local state bound to ViewModel
    var carBrand by remember { mutableStateOf(accountViewModel.carBrand.value) }
    var carSize by remember { mutableStateOf(accountViewModel.carSize.value) }
    val favourites = accountViewModel.favourites

    var message by remember { mutableStateOf<String?>(null) }


    val carSizes = listOf("Hatchback" , "Sedan", "SUV")
    val services = listOf("Wash & Go", "Interior Detail", "Pride Wash", "Pride Valet")

    Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            // Progress
            StepProgressBar(currentStep = 3)
            Spacer(Modifier.height(32.dp))

            // Header
            Text(
                text = "Car Details & Favourites",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(20.dp))

            // Car Brand
            CustomTextField(
                value = carBrand,
                onValueChange = {
                    carBrand = it
                    accountViewModel.carBrand.value = it
                                },
                label = "Car Make",
                leadingIcon = null,
                isPassword = false,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(20.dp))

            // Car Size selection

            carSizes.forEach { size ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(
                            color = if (carSize == size) Color.Green else Color.DarkGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable {
                            carSize = size
                            accountViewModel.carSize.value = size
                        }
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = size,
                        color = if (carSize == size) Color.Black else Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Favourite Services selection
            Text("Select your favourite services", color = Color.White, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(10.dp))
            Column {
                services.forEach { service ->
                    val isSelected = favourites.contains(service)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(
                                color = if (isSelected) Color.Green else Color.DarkGray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                if (isSelected) favourites.remove(service) else favourites.add(service)
                            }
                            .padding(12.dp)
                    ) {
                        Text(
                            text = service,
                            color = if (isSelected) Color.Black else Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
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
                        if (carSize.isNotEmpty() && accountViewModel.favourites.isNotEmpty()) {
                            accountViewModel.saveCarDetails()
                            navController.navigate("preferences")
                        }else {
                            message = "Please fill in all fields"
                        }
                    },
                    containerColor = Color.Green,
                    contentColor = Color.Black,
                    modifier = Modifier.weight(1f),
                )
            }
            message?.let { Text(it, color = Color.Red, modifier = Modifier.padding(top = 10.dp)) }
        }
    }
}