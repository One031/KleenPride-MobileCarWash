package com.example.kleenpride.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kleenpride.ui.theme.KleenPrideTheme
import com.example.kleenpride.ui.theme.LimeGreen
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.kleenpride.R
import com.example.kleenpride.ui.components.CustomButton
import com.example.kleenpride.ui.components.CustomTextField
import com.example.kleenpride.ui.components.OrSeparator
import com.example.kleenpride.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException


/**
 * RegisterActivity is the entry point for user registration
 * Using Jetpack Compose, all UI is defined here
 */

class RegisterActivity : ComponentActivity() {

    // Get the ViewModel
    private val authViewModel: AuthViewModel by viewModels()

    // Google Sign-In client
    private lateinit var googleSignInClient: GoogleSignInClient

    // Launcher for Google Sign-In
    private val googleSignInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            account?.idToken?.let { idToken ->
                // Delegate to ViewModel
                authViewModel.googleLogin(idToken)
            }
        }catch (e: ApiException) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(com.example.kleenpride.R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set the activity content to the Compose UI function
        setContent {
            KleenPrideTheme {
                // Pass viewModel and Google Sign-In function down to UI
                RegisterScreenUI(
                    authViewModel = authViewModel,
                    onGoogleSignInClick = { signInWithGoogle() },
                    onNavigateToLogin = { navigateToLogin() } // Handle navigation to Register
                )
            }
        }
    }
    //Launch Google Sign-In intent
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }
}

/**
 * RegisterScreenUI defines the full registration screen using Compose
 * Observes AuthViewModel for user registration success or errors
 */
@Composable
fun RegisterScreenUI(authViewModel: AuthViewModel? = viewModel(),
                     onGoogleSignInClick: (() -> Unit)? = null,
                     onNavigateToLogin: (() -> Unit)? = null
) {

    // Local state to hold input values
    var firstName by remember { mutableStateOf("John") } // First Name input
    var lastName by remember { mutableStateOf("Doe") } // Last Name input
    var email by remember { mutableStateOf("john.doe@example.com") } // Email input
    var password by remember { mutableStateOf("password") } // Password input

    // Message to show success or error feedback
    var message by remember { mutableStateOf<String?>(null) }

    // Only observe real LiveData if a real ViewModel is provided
    if (authViewModel != null) {
        // Observe LiveData from ViewModel and convert to Compose State
        val user by authViewModel.userLiveData.observeAsState()
        val error by authViewModel.errorLiveData.observeAsState()

        // Side-effect to react to successful registration
        LaunchedEffect(user) {
            if (user != null) {
                // Should navigate to MainActivity
                message = "Registration Successful!"
            }
        }

        // Side-effect to react to errors from ViewModel
        LaunchedEffect(error) {
            error?.let {
                // Display the error message
                message = it
            }
        }
    }

    // Screen background container
    Surface(
        color = Color.Black, // Background colour
        modifier = Modifier
            .fillMaxSize() // Fill the whole screen
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
            .padding(40.dp), // Add padding around the screen edges
            verticalArrangement = Arrangement.Top, // Arrange children from top
            horizontalAlignment = Alignment.CenterHorizontally // Center children horizontally
        ) {
            // Greeting text
            Text(
                text = "Hello there",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
            // Header text for the screen
            Text(
                text = "Create an Account",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(25.dp)) // Space between header and fields

            // Input fields
            CustomTextField(
                value = firstName, // Value bound to state
                onValueChange = { firstName = it }, // Update state on input change
                label = "First Name", //Field label
                leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) }, // Icon
            )
            Spacer(modifier = Modifier.height(10.dp))

            CustomTextField(
                value = lastName, // Value bound to state
                onValueChange = { lastName = it }, // Update state on input change
                label = "Last Name", //Field label
                leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) }, // Icon
            )
            Spacer(modifier = Modifier.height(10.dp))

            CustomTextField(
                value = email, // Value bound to state
                onValueChange = { email = it }, // Update state on input change
                label = "Email", //Field label
                leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) }, // Icon
            )
            Spacer(modifier = Modifier.height(10.dp))

            CustomTextField(
                value = password, // Value bound to state
                onValueChange = { password = it }, // Update state on input change
                label = "Password", //Field label
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) }, // Icon
                isPassword = true // Password field
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Register Button
            CustomButton(
                text = "Register",
                onClick = {
                    // Validate inputs before calling ViewModel
                    if (firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                        // Delegate registration to AuthViewModel
                        authViewModel?.register(email, password)
                    }else {
                        // Show error if fields are empty
                        message = "Please fill in all fields"
                    }
                },
                    containerColor = Green,
                    contentColor = Color.Black
            )

            // OR Separator
            OrSeparator()

            // Google Sign-In Button
            Button(
                onClick = { onGoogleSignInClick?.invoke() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth(0.7f), // Full-width button
                shape = RoundedCornerShape(30.dp)
            ){
                // Show the Google icon
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google Icon",
                    tint = Color.Unspecified, // keep the orginal Google Colors
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = "Continue with Google")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Navigate to Login screen
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Already have an account?",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Login",
                    color = Color.Green,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .clickable { onNavigateToLogin?.invoke() }
                )
            }

            // Feedback message (success or error)
            message?.let {
                Text(
                    text = it,
                    color = Color.Red, // Red for errors
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }

    }


}

/**
 * Preview function
 * Calls RegisterScreenUI with no ViewModel so it can render
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreenUI(authViewModel = null)
}