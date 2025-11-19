package com.example.kleenpride.admin.ui.detailers

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kleenpride.admin.data.models.Detailer
import com.example.kleenpride.admin.ui.overview.AdminTopBar
import com.example.kleenpride.admin.viewmodel.AdminDetailersViewModel
import com.example.kleenpride.ui.auth.LoginActivity
import com.example.kleenpride.ui.theme.LimeGreen
import kotlinx.coroutines.delay
import java.util.regex.Pattern

class AdminDetailersActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { AdminDetailersScreen() }
    }
}

@Composable
fun AdminDetailersScreen() {
    val context = LocalContext.current
    val viewModel: AdminDetailersViewModel = viewModel()
    val detailers by viewModel.detailers.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val snackbarHostState = remember { SnackbarHostState() }
    val error by viewModel.error.observeAsState()
    val createSuccess by viewModel.createSuccess.observeAsState(false)
    val needsReLogin by viewModel.needsReLogin.observeAsState(false)

    var showAddDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf<String?>(null) }

    // Handle re-login requirement
    LaunchedEffect(needsReLogin) {
        if (needsReLogin) {
            snackbarHostState.showSnackbar(
                message = "Detailer created! Redirecting to login...",
                duration = SnackbarDuration.Short
            )
            delay(1500) // Give user time to read the message

            // Navigate to login activity
            // Replace with your actual LoginActivity class
            val intent = Intent(context, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            context.startActivity(intent)

            viewModel.resetReLoginFlag()
        }
    }

    // Handle success message and auto-close dialog
    LaunchedEffect(createSuccess) {
        if (createSuccess) {
            showAddDialog = false // Close dialog immediately
            viewModel.resetCreateSuccess()
        }
    }

    // Handle errors
    LaunchedEffect(error) {
        error?.let { errorMessage ->
            if (!errorMessage.contains("Redirecting to login")) {
                snackbarHostState.showSnackbar(
                    message = errorMessage,
                    duration = SnackbarDuration.Long
                )
            }
            viewModel.resetError()
        }
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = { AdminTopBar() },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = if (data.visuals.message.contains("Error") ||
                        data.visuals.message.contains("Failed"))
                        Color.Red else LimeGreen,
                    contentColor = Color.White
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .background(Color.Black)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            DetailersHeader(
                total = detailers.size,
                onAddClick = { showAddDialog = true }
            )

            Spacer(Modifier.height(12.dp))

            // Show loading indicator
            if (isLoading && detailers.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = LimeGreen)
                }
            } else {
                // Show detailers list
                detailers.forEach { detailer ->
                    DetailerCard(
                        detailer = detailer,
                        onToggleStatus = {
                            val newStatus = if (detailer.status == "ACTIVE") "INACTIVE" else "ACTIVE"
                            viewModel.updateDetailerStatus(detailer.id, newStatus)
                        },
                        onDelete = {
                            showDeleteConfirmDialog = detailer.id
                        }
                    )
                }
            }

            Spacer(Modifier.height(80.dp))
        }

        if (showAddDialog) {
            AddDetailerDialog(
                onDismiss = { showAddDialog = false },
                onSave = { firstName, lastName, phone, email, password ->
                    viewModel.createDetailer(
                        email = email,
                        password = password,
                        firstName = firstName,
                        lastName = lastName,
                        phoneNumber = phone
                    )
                }
            )
        }

        // Delete confirmation dialog
        showDeleteConfirmDialog?.let { detailerId ->
            AlertDialog(
                onDismissRequest = { showDeleteConfirmDialog = null },
                containerColor = Color(0xFF111111),
                title = {
                    Text(
                        "Delete Detailer?",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        "This will permanently delete this detailer. This action cannot be undone.",
                        color = Color.Gray
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteDetailer(detailerId)
                            showDeleteConfirmDialog = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteConfirmDialog = null }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }
    }
}

@Composable
fun DetailersHeader(total: Int, onAddClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                "All Detailers",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                "$total registered",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }

        Button(
            onClick = onAddClick,
            colors = ButtonDefaults.buttonColors(containerColor = LimeGreen),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("+ Add Detailer", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DetailerCard(
    detailer: Detailer,
    onToggleStatus: () -> Unit,
    onDelete: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .background(Color(0xFF0F0F0F), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(LimeGreen, RoundedCornerShape(24.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        detailer.firstName.firstOrNull()?.toString() ?: "D",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            detailer.displayName,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.width(8.dp))
                        StatusChip(text = detailer.status, color = detailer.statusColor)
                    }

                    Spacer(Modifier.height(4.dp))

                    Text(detailer.email, color = Color.Gray, fontSize = 13.sp)
                    Text(detailer.phoneNumber, color = Color.Gray, fontSize = 13.sp)
                }
            }

            Box {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = "More options",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { menuExpanded = true }
                )
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(if (detailer.status == "ACTIVE") "Deactivate" else "Activate") },
                        onClick = {
                            onToggleStatus()
                            menuExpanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete", color = Color.Red) },
                        onClick = {
                            onDelete()
                            menuExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DetailerStatBox(
                label = "Rating",
                value = "%.1f".format(detailer.rating),
                valueColor = Color.White,
                icon = Icons.Default.Star
            )

            DetailerStatBox(
                label = "Jobs",
                value = detailer.totalJobs.toString(),
                valueColor = Color.White,
                icon = null
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF0A0A0A), RoundedCornerShape(8.dp))
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Earnings", color = Color.Gray, fontSize = 11.sp)
                Spacer(Modifier.height(4.dp))
                Text(
                    "R${String.format("%,d", detailer.earnings)}",
                    color = LimeGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun RowScope.DetailerStatBox(
    label: String,
    value: String,
    valueColor: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector?
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .background(Color(0xFF0A0A0A), RoundedCornerShape(8.dp))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(icon, contentDescription = null, tint = LimeGreen, modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
            }
            Text(label, color = Color.Gray, fontSize = 11.sp)
        }
        Spacer(Modifier.height(4.dp))
        Text(value, color = valueColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun StatusChip(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.2f), shape = RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AddDetailerDialog(
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String) -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    fun isEmailValid(email: String) = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+").matcher(email).matches()
    fun isPhoneValid(phone: String) = Pattern.compile("^\\+?[0-9]{10,15}\$").matcher(phone).matches()

    val isFormValid = firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            phone.isNotBlank() && isPhoneValid(phone) &&
            email.isNotBlank() && isEmailValid(email) &&
            password.length >= 6

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF111111),
        title = {
            Text("Add New Detailer", color = Color.White, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name", color = Color.Gray) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1A1A1A),
                        unfocusedContainerColor = Color(0xFF1A1A1A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = LimeGreen
                    )
                )

                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name", color = Color.Gray) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1A1A1A),
                        unfocusedContainerColor = Color(0xFF1A1A1A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = LimeGreen
                    )
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number", color = Color.Gray) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1A1A1A),
                        unfocusedContainerColor = Color(0xFF1A1A1A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = LimeGreen
                    )
                )
                if (phone.isNotBlank() && !isPhoneValid(phone)) {
                    Text("Invalid phone format", color = Color.Red, fontSize = 12.sp)
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address", color = Color.Gray) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1A1A1A),
                        unfocusedContainerColor = Color(0xFF1A1A1A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = LimeGreen
                    )
                )
                if (email.isNotBlank() && !isEmailValid(email)) {
                    Text("Invalid email format", color = Color.Red, fontSize = 12.sp)
                }

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = Color.Gray) },
                    singleLine = true,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                        Icon(
                            imageVector = image,
                            contentDescription = null,
                            modifier = Modifier.clickable { passwordVisible = !passwordVisible },
                            tint = LimeGreen
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF1A1A1A),
                        unfocusedContainerColor = Color(0xFF1A1A1A),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = LimeGreen
                    )
                )
                if (password.isNotBlank() && password.length < 6) {
                    Text("Password must be at least 6 characters", color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(firstName, lastName, phone, email, password) },
                enabled = isFormValid,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) LimeGreen else Color.Gray
                )
            ) {
                Text("Save", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Color.Gray)
            }
        }
    )
}