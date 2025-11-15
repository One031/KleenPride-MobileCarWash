package com.example.kleenpride.ui.profile

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Brush.Companion.horizontalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.kleenpride.R
import com.example.kleenpride.ui.components.BottomNavBar
import com.example.kleenpride.ui.components.CustomButton
import com.example.kleenpride.ui.theme.KleenPrideTheme
import com.example.kleenpride.ui.theme.LimeGreen
import com.example.kleenpride.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen( viewModel: ProfileViewModel = viewModel()) {
    val context = LocalContext.current

    val email = viewModel.email.value
    val isUploadingImage = viewModel.isUploadingImage.value
    val profileImageUri = viewModel.profileImageUrl.value
    val initials = viewModel.initials.value

   val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.uploadProfileImage(
                uri = it,
                onSuccess = {
                    Toast.makeText(context, "Profile image updated", Toast.LENGTH_SHORT).show()
                },
                onError = { error ->
                    Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                }
            )
        }
   }


    Surface(
        color = Color.Black,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(30.dp)
                    .padding(bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Greeting Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hi, ${
                            viewModel.fullName.value.split(" ").firstOrNull() ?: "User"
                        }!👋",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 40.sp
                    )
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(CircleShape)
                            .background(Color.DarkGray.copy(alpha = 0.5f))
                            .border(2.dp, LimeGreen, CircleShape)
                            .clickable {
                                if (!isUploadingImage) {
                                    imagePickerLauncher.launch("image/*")
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isUploadingImage) {
                            CircularProgressIndicator(
                                color = LimeGreen,
                                modifier = Modifier.size(30.dp)
                            )
                        } else if (profileImageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(profileImageUri),
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize().clip(CircleShape)
                            )
                        } else {
                            Text(
                                text = initials,
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                HorizontalDivider(
                    thickness = 2.dp,
                    color = LimeGreen.copy(alpha = 0.4f),
                    modifier = Modifier.background(
                        horizontalGradient(
                            listOf(
                                LimeGreen,
                                Color(0xFFB2FF59)
                            )
                        )
                    )
                )

                // Account Info Section
                RoundedSection(
                    title = "Account Information",
                    content = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            InfoRow(Icons.Filled.Email, email)
                            InfoRow(Icons.Filled.Lock, "••••••••")
                            InfoRow(Icons.Filled.AccountBalanceWallet, "Payment Details")
                        }
                    },
                    buttonText = "Update Info",
                    onClick = {
                        val intent = Intent(
                            context,
                            com.example.kleenpride.ui.profile.accinfo.AccountDetailsActivity::class.java
                        )
                        context.startActivity(intent)
                    }
                )

                // My Garage Button
                MyGarageButton(
                    onClick = {
                        val intent = Intent(
                            context,
                            com.example.kleenpride.ui.profile.garage.MyGarageActivity::class.java
                        )
                        context.startActivity(intent)
                    }
                )

                // Locations Button
                LocationsButton(
                    onClick = {
                        val intent = Intent(
                            context,
                            com.example.kleenpride.ui.profile.location.MyLocationsActivity::class.java
                        )
                        context.startActivity(intent)
                    }
                )

                // Preferences Section
                RoundedSection(
                    title = "Preferences",
                    content = {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            PreferenceToggle(
                                "Enable Notifications",
                                viewModel.enableNotifications.value
                            ) {
                                viewModel.enableNotifications.value = it
                                viewModel.savePreferences(
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Preference saved",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                            PreferenceToggle(
                                "Receive Promotions",
                                viewModel.receivePromotions.value
                            ) {
                                viewModel.receivePromotions.value = it
                                viewModel.savePreferences(
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Preference saved",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                            PreferenceToggle(
                                "Receive Reminders",
                                viewModel.receiveReminders.value
                            ) {
                                viewModel.receiveReminders.value = it
                                viewModel.savePreferences(
                                    onSuccess = {
                                        Toast.makeText(
                                            context,
                                            "Preference saved",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                        }
                    }
                )

                // Log Out Button
                Button(
                    onClick = {
                        viewModel.signOut()
                        val intent = Intent(
                            context,
                            com.example.kleenpride.ui.auth.LoginActivity::class.java
                        )
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFF3B30)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = "Logout",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Log Out", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
            // Bottom Navigation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
            ) {
                BottomNavBar(currentScreen = "profile")
            }
        }
    }
}

// Updated CustomButton to support icon
@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        modifier = modifier
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null, tint = contentColor)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(text, color = contentColor)
    }
}



@Composable
fun InfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = LimeGreen, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, color = Color.White, fontSize = 16.sp)
    }
}

@Composable
fun RoundedSection(title: String, content: @Composable ColumnScope.() -> Unit, buttonText: String? = null, onClick: (() -> Unit) ? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.linearGradient(listOf(Color(0xFF0E0E0E), Color(0xFF1A1A1A))),
                RoundedCornerShape(20.dp))
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(title, color = Color.White, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
        content()

        if (buttonText != null && onClick != null) {
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LimeGreen)
            ) {
                Text(buttonText, color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun PreferenceToggle(title: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = Color.White)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = LimeGreen,
                checkedTrackColor = LimeGreen.copy(alpha = 0.4f),
                uncheckedThumbColor = Color.Gray
            )
        )
    }
}

@Composable
fun MyGarageButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(158.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, LimeGreen, RoundedCornerShape(20.dp))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.garage),
                contentDescription = "My Garage",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "My Garage",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.Center)
                    .background(Color.DarkGray.copy(alpha = 0.7f), shape = RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
fun LocationsButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(158.dp)
            .clip(RoundedCornerShape(20.dp))
            .border(2.dp, LimeGreen, RoundedCornerShape(20.dp))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.location),
                contentDescription = "Locations",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "Locations",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.Center)
                    .background(Color.DarkGray.copy(alpha = 0.7f), shape = RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    KleenPrideTheme {
        ProfileScreen()
    }
}
