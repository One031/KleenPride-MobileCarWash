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
import androidx.compose.material.icons.filled.Edit
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
import com.example.kleenpride.ui.components.AccountField
import com.example.kleenpride.ui.components.BottomNavBar
import com.example.kleenpride.ui.components.ChangePasswordDialog
import com.example.kleenpride.ui.components.ChangeEmailDialog
import com.example.kleenpride.ui.components.ChangeFullNameDialog
import com.example.kleenpride.ui.components.CustomButton
import com.example.kleenpride.ui.components.CustomTextField
import com.example.kleenpride.ui.profile.payment.AddPayFastCardSheet
import com.example.kleenpride.ui.profile.payment.PaymentCard
import com.example.kleenpride.ui.theme.KleenPrideTheme
import com.example.kleenpride.ui.theme.LimeGreen
import com.example.kleenpride.viewmodel.AccountDetailsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsScreen(viewModel: AccountDetailsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    val context = LocalContext.current

    var editCardIndex by remember {mutableStateOf<Int?>(null)}
    var editCard by remember { mutableStateOf<PaymentCard?>(null) }

    var showEmailDialog by remember { mutableStateOf(false) }
    var showPasswordDialog by remember { mutableStateOf(false) }
    var showFullNameDialog by remember { mutableStateOf(false) }
    var showDeleteConfirm by remember { mutableStateOf<Int?>(null) }


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

               // Full Name with edit button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    AccountField(
                        "Full Name",
                        viewModel.fullName.value,
                        {},
                        Icons.Filled.Person,
                        modifier = Modifier.weight(1f),
                        enabled = false
                    )
                    IconButton(
                        onClick = { showFullNameDialog = true },
                        modifier = Modifier
                            .size(56.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(Icons.Filled.Edit, "Edit Full Name", tint = LimeGreen)
                    }
                }

                // Email with edit button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AccountField(
                        "Email",
                        viewModel.email.value,
                        {},
                        Icons.Filled.Email,
                        modifier = Modifier.weight(1f),
                        enabled = false
                    )
                    IconButton(
                        onClick = { showEmailDialog = true },
                        modifier = Modifier
                            .size(56.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(Icons.Filled.Edit, "Edit Email", tint = LimeGreen)
                    }
                }

                // Password with edit button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AccountField(
                        "Password",
                        "••••••••",
                        {},
                        Icons.Filled.Lock,
                        isPassword = true,
                        modifier = Modifier.weight(1f),
                        enabled = false
                    )
                    IconButton(
                        onClick = { showPasswordDialog = true },
                        modifier = Modifier
                            .size(56.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Icon(Icons.Filled.Edit, "Edit Password", tint = LimeGreen)
                    }
                }

                // Payment Details Section
                Text(
                    text = "Payment Details",
                    color = LimeGreen,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                if (viewModel.isLoadingCards.value) {
                    CircularProgressIndicator(
                        color = LimeGreen,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }else {

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    viewModel.cards.forEachIndexed { index, card ->
                        val isSelected = index == viewModel.selectedCardIndex.value

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.selectCard(index) },
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        brush = if (isSelected)
                                            Brush.linearGradient(
                                                listOf(
                                                    Color(0xFF1C1C1C),
                                                    Color(0xFF363636)
                                                )
                                            )
                                        else
                                            Brush.linearGradient(
                                                listOf(
                                                    Color(0xFF0E0E0E),
                                                    Color(0xFF1A1A1A)
                                                )
                                            ),
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
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (card.brand.isNotEmpty()) "${card.brand} •••• ${card.cardNumber}"
                                            else
                                                "•••• ${card.cardNumber}",
                                            color = Color.White,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 16.sp
                                        )

                                        if (card.cardAlias.isNotEmpty()) {
                                            Text(
                                                text = card.cardAlias,
                                                color = Color.Gray,
                                                fontSize = 14.sp
                                            )
                                        }

                                        if (isSelected) {
                                            Text(
                                                text = "Default",
                                                color = LimeGreen,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }

                                    Row {
                                        TextButton(onClick = {
                                            editCardIndex = index
                                            editCard = card
                                            coroutineScope.launch { sheetState.show() }
                                        }) {
                                            Text("Edit", color = LimeGreen)
                                        }
                                        TextButton(onClick = {
                                            showDeleteConfirm = index
                                        }) {
                                            Text("Delete", color = Color.Red)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                    // Add Card Button
                    Button(
                        onClick = {
                            editCardIndex = null
                            editCard = PaymentCard("", "", "", "")
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
                    onClick = {
                        viewModel.saveAccountInfo(
                            onSuccess = {
                                Toast.makeText(
                                    context,
                                    "Account details updated",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            onError = { error ->
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Black
                    ),
                    enabled = !viewModel.isSaving.value
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
                            text = if (viewModel.isSaving.value) "Saving..." else "Save Changes",
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
            AddPayFastCardSheet(
                existingCard = if (editCardIndex != null) editCard else null,
                onCardAdded = { card ->
                    viewModel.addOrUpdateCard(card, editCardIndex)
                    editCard = null
                    coroutineScope.launch { sheetState.hide() }
                    val message = if (editCardIndex != null) "Card Updated" else "Card Added"
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                },
                onCancel = {
                    editCard = null
                    coroutineScope.launch { sheetState.hide() }
                }
            )
        }
    }

    // Password Change Dialog
    if (showPasswordDialog) {
        ChangePasswordDialog(
            onDismiss = { showPasswordDialog = false },
            onConfirm = { currentPassword, newPassword ->
                viewModel.updatePassword(
                    currentPassword = currentPassword,
                    newPassword = newPassword,
                    onSuccess = {
                        showPasswordDialog = false
                        Toast.makeText(
                            context,
                            "Password updated successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onError = { error ->
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    }
                )
            }
        )
    }

    // Full Name Change Dialog
    if (showFullNameDialog) {
        ChangeFullNameDialog(
          currentFullName = viewModel.fullName.value,
            onDismiss = { showFullNameDialog = false },
            onConfirm = { newFullName ->
                viewModel.fullName.value = newFullName
                showFullNameDialog = false
                Toast.makeText(context, "Name updated", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // Email Change Dialog
    if (showEmailDialog) {
        ChangeEmailDialog(
            currentEmail = viewModel.email.value,
            onDismiss = { showEmailDialog = false },
            onConfirm = { newEmail, password ->
                viewModel.updateEmail(
                    newEmail = newEmail,
                    currentPassword = password,
                    onSuccess = {
                        showEmailDialog = false
                        Toast.makeText(
                            context,
                            "Email updated. Please verify your new email.",
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    onError = { error ->
                        Toast.makeText(context, error, Toast.LENGTH_LONG).show()
                    }
                )
            }
        )
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirm != null) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = null },
            title = { Text("Delete Card", color = Color.White) },
            text = { Text("Are you sure you want to delete this card?", color = Color.Gray) },
            confirmButton = {
                TextButton(onClick = {
                    val index = showDeleteConfirm!!
                    viewModel.deleteCard(
                        index = index,
                        onSuccess = {
                            Toast.makeText(context, "Card deleted", Toast.LENGTH_SHORT).show()
                            showDeleteConfirm = null
                        },
                        onError = { error ->
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            showDeleteConfirm = null
                        }
                    )
                }) {
                    Text("Delete", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = null }) {
                    Text("Cancel", color = LimeGreen)
                }
            },
            containerColor = Color(0xFF1A1A1A)
        )
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountDetailsScreenPreview() {
    KleenPrideTheme {
        AccountDetailsScreen()
    }
}
