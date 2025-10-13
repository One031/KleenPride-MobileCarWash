package com.example.kleenpride.ui.auth.setup

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.kleenpride.R
import com.example.kleenpride.ui.components.CustomButton
import com.example.kleenpride.ui.components.CustomTextField
import com.example.kleenpride.ui.components.OrSeparator
import com.example.kleenpride.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.kleenpride.viewmodel.AccountSetupViewModel

/**
 * Account Setup step 1
 * Replicates the functionality of the previous RegisterActivity
 */
@Composable
fun BasicInfo(navController: NavController, authViewModel: AuthViewModel = viewModel(), accountViewModel: AccountSetupViewModel = viewModel()) {

    // Local state for input fields
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var message by remember { mutableStateOf<String?>(null) }

    // Observe Firebase user and error state
    val user by authViewModel.userLiveData.observeAsState()
    val error by authViewModel.errorLiveData.observeAsState()

    // Google Sign-In setup
    val context = LocalContext.current
    var googleSignInClient by remember { mutableStateOf<GoogleSignInClient?>(null) }

    LaunchedEffect(Unit) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    val googleSignInLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken -> authViewModel.googleLogin(idToken) }
        } catch (e: ApiException) { e.printStackTrace() }
    }

    fun signInWithGoogle() { googleSignInClient?.signInIntent?.let { googleSignInLauncher.launch(it) } }


        // Side-effect to react to successful registration
        LaunchedEffect(user) {
            user?.let {
                // Show a toast
                Toast.makeText(context, "Registration Successful!", Toast.LENGTH_SHORT).show()
                // Navigate after registration
                navController.navigate("personal_info") {
                    popUpTo("basic_info") { inclusive = true }
                }
            }
        }

        // Side-effect to react to errors from ViewModel
        LaunchedEffect(error) {
            error?.let {
                // Display the error message
                message = it
            }
        }

    // Screen UI
    Surface(color = Color.Black, modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Progress
            StepProgressBar(currentStep = 1)
            Spacer(Modifier.height(32.dp))

            // Header
            Text(
                text = "Basic Information",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(20.dp))

            // Continue with Google
            Button(
                onClick = { signInWithGoogle() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                modifier = Modifier.fillMaxWidth(0.7f),
                shape = RoundedCornerShape(30.dp)
            ) {
                Icon(painterResource(id = R.drawable.ic_google), contentDescription = "Google Icon", tint = Color.Unspecified, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Continue with Google")
            }

            Spacer(Modifier.height(16.dp))
            OrSeparator()
            Spacer(Modifier.height(16.dp))

            // Fields
            CustomTextField(accountViewModel.firstName.value, { accountViewModel.firstName.value = it }, "First Name", leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) })
            Spacer(Modifier.height(10.dp))
            CustomTextField(accountViewModel.lastName.value, { accountViewModel.lastName.value = it }, "Last Name", leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) })
            Spacer(Modifier.height(10.dp))
            CustomTextField(email, { email = it }, "Email", leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) })
            Spacer(Modifier.height(10.dp))
            CustomTextField(password, { password = it }, "Password", leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) }, isPassword = true)
            Spacer(Modifier.height(20.dp))

            // Next button
            CustomButton(
                text = "Next",
                onClick = {
                    if (accountViewModel.firstName.value.isNotEmpty() &&
                        accountViewModel.lastName.value.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() ) {
                        // Register user
                        authViewModel.register(email, password) {
                            accountViewModel.saveBasicInfo()
                        }
                }else {
                        message = "Please fill in all fields"
                    }
                },
                containerColor = Color.Green,
                contentColor = Color.Black
            )

            message?.let { Text(it, color = Color.Red, modifier = Modifier.padding(top = 10.dp)) }
        }
    }
}