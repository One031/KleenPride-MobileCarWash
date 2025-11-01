package com.example.kleenpride.ui.profile.accinfo

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.horizontalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kleenpride.ui.components.BottomNavBar
import com.example.kleenpride.ui.components.CustomButton
import com.example.kleenpride.ui.components.CustomTextField
import com.example.kleenpride.ui.theme.KleenPrideTheme
import com.example.kleenpride.ui.theme.LimeGreen
import kotlinx.coroutines.launch

data class PaymentCard(
    var nameOnCard: String,
    var cardNumber: String,
    var expiryDate: String,
    var cvv: String,
    var brand: String = "Visa"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsScreen() {
    var fullName by remember { mutableStateOf("Zion Cummings") }
    var email by remember { mutableStateOf("zion@example.com") }
    var password by remember { mutableStateOf("password123") }

    val context = LocalContext.current
    val cards = remember {
        mutableStateListOf(
            PaymentCard("Zion Cummings", "4111111111117812", "12/26", "123", "Visa"),
            PaymentCard("Zion Cummings", "5500000000003456", "08/25", "456", "Mastercard")
        )
    }

    var selectedCard by remember { mutableStateOf(cards.first()) }
    var editCard by remember { mutableStateOf<PaymentCard?>(null) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {

            // Main scrollable content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(30.dp)
                    .padding(bottom = 80.dp), // space for bottom nav
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Account Details",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp
                )
                HorizontalDivider(
                    thickness = 2.dp,
                    color = LimeGreen.copy(alpha = 0.4f),
                    modifier = Modifier.background(
                        horizontalGradient(listOf(LimeGreen, Color(0xFFB2FF59)))
                    )
                )

                // Account Info Fields
                AccountField("Full Name", fullName, { fullName = it }, Icons.Filled.Person)
                AccountField("Email", email, { email = it }, Icons.Filled.Email)
                AccountField(
                    "Password",
                    password,
                    { password = it },
                    Icons.Filled.Lock,
                    isPassword = true
                )

                // Payment Details Section
                Text(
                    text = "Payment Details",
                    color = LimeGreen,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    cards.forEach { card ->
                        val isSelected = selectedCard == card

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedCard = card },
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        brush = if (isSelected)
                                            Brush.linearGradient(listOf(Color(0xFF1C1C1C), Color(0xFF363636)))
                                        else
                                            Brush.linearGradient(listOf(Color(0xFF0E0E0E), Color(0xFF1A1A1A))),
                                        RoundedCornerShape(16.dp)
                                    )
                                    .padding(16.dp)
                                    .fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "${card.brand} •••• ${card.cardNumber.takeLast(4)}",
                                            color = Color.White,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                        Text(
                                            text = card.nameOnCard,
                                            color = Color.Gray,
                                            fontSize = 14.sp
                                        )
                                    }

                                    TextButton(onClick = {
                                        editCard = card
                                        coroutineScope.launch { sheetState.show() }
                                    }) {
                                        Text("Edit", color = LimeGreen)
                                    }
                                }
                            }
                        }
                    }

                    // Add Card Button
                    Button(
                        onClick = {
                            editCard = PaymentCard("", "", "", "", "")
                            coroutineScope.launch { sheetState.show() }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        )
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = horizontalGradient(
                                        colors = listOf(LimeGreen, Color(0xFFB2FF59))
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Add Card",
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontSize = 16.sp
                            )
                        }
                    }
                }


                Button(
                    onClick = { Toast.makeText(context, "Saved Changes", Toast.LENGTH_SHORT).show() }, //saves acc info
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = horizontalGradient(
                                    colors = listOf(LimeGreen, Color(0xFFB2FF59))
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Save Changes",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            //Bottom Nav Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                BottomNavBar(currentScreen = "accountinfo")
            }
        }
    }

    // Bottom Sheet for Add/Edit Card
    if (editCard != null) {
        ModalBottomSheet(
            onDismissRequest = {
                coroutineScope.launch { sheetState.hide() }
                editCard = null
            },
            sheetState = sheetState,
            containerColor = Color(0xFF101010),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            EditCardSheet(
                card = editCard!!,
                onSave = { newCard ->
                    if (newCard.cvv.length == 3 && newCard.cvv.all { it.isDigit() }) {
                        if (cards.contains(editCard)) {
                            val index = cards.indexOf(editCard)
                            cards[index] = newCard
                        } else cards.add(newCard)
                        editCard = null
                        coroutineScope.launch { sheetState.hide() }
                    }
                },
                onDelete = {
                    cards.remove(editCard)
                    editCard = null
                    coroutineScope.launch { sheetState.hide() }
                },
                onCancel = {
                    editCard = null
                    coroutineScope.launch { sheetState.hide() }
                }
            )
        }
    }
}


@Composable
fun EditCardSheet(
    card: PaymentCard,
    onSave: (PaymentCard) -> Unit,
    onDelete: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    var nameOnCard by remember { mutableStateOf(card.nameOnCard) }
    var cardNumber by remember { mutableStateOf(card.cardNumber) }
    var expiry by remember { mutableStateOf(card.expiryDate) }
    var cvv by remember { mutableStateOf(card.cvv) }
    var brand by remember { mutableStateOf(card.brand) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (card.nameOnCard.isEmpty()) "Add Card" else "Edit Card",
            color = LimeGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )

        CustomTextField(value = nameOnCard, onValueChange = { nameOnCard = it }, label = "Name on Card")
        CustomTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = "Card Number",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            CustomTextField(
                value = expiry,
                onValueChange = { expiry = it },
                label = "Expiry (MM/YY)",
                modifier = Modifier.weight(1f)
            )
            CustomTextField(
                value = cvv,
                onValueChange = {
                    if (it.length <= 3 && it.all { c -> c.isDigit() }) cvv = it
                },
                label = "CVV",
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }
        CustomTextField(value = brand, onValueChange = { brand = it }, label = "Card Brand (Visa, Mastercard)")

        Spacer(modifier = Modifier.height(20.dp))
        CustomButton(
            text = "Save",
            onClick = { onSave(PaymentCard(nameOnCard, cardNumber, expiry, cvv, brand))
                        Toast.makeText(context,"Card Added", Toast.LENGTH_SHORT).show() },
            containerColor = LimeGreen,
            contentColor = Color.Black,
            modifier = Modifier.fillMaxWidth()
        )

        if (card.nameOnCard.isNotEmpty()) {
            CustomButton(
                text = "Delete",
                onClick = onDelete,
                containerColor = Color.Red,
                contentColor = Color.White,
                modifier = Modifier.fillMaxWidth()
            )
        }

        CustomButton(
            text = "Cancel",
            onClick = onCancel,
            containerColor = Color.Gray,
            contentColor = Color.White,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun AccountField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = LimeGreen) },
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        tint = LimeGreen
                    )
                }
            }
        },
        label = { Text(label, color = Color.Gray) },
        textStyle = LocalTextStyle.current.copy(color = Color.White),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            cursorColor = LimeGreen
        ),
        visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(listOf(Color(0xFF0E0E0E), Color(0xFF1A1A1A))),
                RoundedCornerShape(16.dp)
            )
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountDetailsScreenPreview() {
    KleenPrideTheme {
        AccountDetailsScreen()
    }
}
