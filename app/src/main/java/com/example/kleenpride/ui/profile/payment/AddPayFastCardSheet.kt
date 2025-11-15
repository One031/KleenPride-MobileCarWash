package com.example.kleenpride.ui.profile.payment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.example.kleenpride.ui.components.CustomButton
import com.example.kleenpride.ui.components.CustomTextField
import com.example.kleenpride.ui.components.CustomTextFieldCard
import com.example.kleenpride.ui.theme.LimeGreen
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun AddPayFastCardSheet(
    existingCard: PaymentCard? = null,
    onCardAdded: (PaymentCard) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser
    val scope = rememberCoroutineScope()

    var cardAlias by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var isProcessing by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }


    val payFastManager = remember { MockPayFastManager(context) }
    val isEditing = existingCard?.token?.isNotEmpty() == true

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (isEditing) "Edit Payment Method" else "Add Payment Method",
            color = LimeGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        Text(
            text = "Enter a name for your card (e.g., 'Personal Visa')",
            color = Color.Gray,
            fontSize = 14.sp
        )

        CustomTextFieldCard(
            value = cardAlias,
            onValueChange = { cardAlias = it },
            label = "Card Nickname",
            placeholder ="e.g., Personal Visa",
            enabled = !isProcessing
        )

        CustomTextFieldCard(
            value = cardNumber,
            onValueChange = {
                // Only allow digits and limit to 4 characters
                if(it.length <= 4 && it.all { char -> char.isDigit() }) {
                    cardNumber = it
                }
            },
            label = "Last 4 digits",
            placeholder = "1234",
            enabled = !isProcessing && !isEditing
        )

        if (isEditing) {
            Text(
                text = "Note: Card number cannot be changed",
                color = Color.Gray,
                fontSize = 12.sp
            )
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                fontSize = 14.sp
            )
        }

        if (isProcessing) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    color = LimeGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Processing...",
                    color = LimeGreen,
                    fontSize = 14.sp
                )
            }
        }

        CustomButton(
            text = if (isProcessing) "Processing..." else if (isEditing) "Update Card" else "Add Card via PayFast",
            onClick = {
                // Validation
                if (cardAlias.isBlank()) {
                    errorMessage = "Please enter a card nickname"
                    return@CustomButton
                }

                if (cardAlias.length < 3) {
                    errorMessage = "Card nickname must be at least 3 characters"
                    return@CustomButton
                }

                if (!isEditing && cardNumber.length != 4) {
                    errorMessage = "Enter 4 digits"
                    return@CustomButton
                }

                isProcessing = true
                errorMessage = null

                if (isEditing) {
                    val updatedCard = PaymentCard(
                        cardAlias = cardAlias,
                        cardNumber = existingCard?.cardNumber ?: cardNumber,
                        token = existingCard?.token ?: "",
                        brand = existingCard?.brand ?: "Visa"
                    )
                    onCardAdded(updatedCard)
                    isProcessing = false
                }else {

                    // Get user email
                    val userEmail = currentUser?.email ?: "user@example.com"

                    // Simulate card tokenization
                    scope.launch {
                        payFastManager.requestTokenization(
                            userEmail = userEmail,
                            cardAlias = cardAlias,
                            cardNumber = cardNumber,
                            onTokenReceived = { token ->
                                // Create card with mock data
                                val card = PaymentCard(
                                    cardAlias = cardAlias,
                                    cardNumber = cardNumber,
                                    token = token,
                                    brand = "Visa"
                                )
                                onCardAdded(card)
                                isProcessing = false
                            },
                            onError = { error ->
                                errorMessage = error
                                isProcessing = false
                            }
                        )
                    }
                }
            },
            containerColor = LimeGreen,
            contentColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        )

        CustomButton(
            text = "Cancel",
            onClick = onCancel,
            containerColor = Color.Gray,
            contentColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        )

       // Info Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "This is a mock payment system for testing",
                    color = LimeGreen.copy(alpha = 0.8f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}