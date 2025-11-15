package com.example.kleenpride.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kleenpride.R
import com.example.kleenpride.ui.components.CustomButton
import com.example.kleenpride.ui.components.CustomTextField
import com.example.kleenpride.ui.components.OrSeparator
import com.example.kleenpride.ui.theme.KleenPrideTheme
import com.example.kleenpride.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

/**
 * LoginActivity allows users to log into their account
 * Uses Jetpack Compose + Firebase Auth through AuthViewModel
 */

class LoginActivity : ComponentActivity() {
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
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure Google Sign-In (ID token required for Firebase auth)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        /**
         * Compose content: pass two lambdas into the UI:
         * onGoogleSignInClick launches Google sign-in
         * onNavigateToRegister opens RegisterActivity
         */

        setContent {
            KleenPrideTheme {
                LoginScreenUI(
                    authViewModel = authViewModel,
                    onGoogleSignInClick = { signInWithGoogle() }, // Handle Google Sign-In
                )
            }
        }
    }

    // Launches Google Sign-In flow
    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

}

/**
 * LoginScreenUI defines the full login screen using Compose
 * authViewModel is passed down to the UI
 * onGoogleSignInClick and onNavigateToRegister are lambdas to handle button clicks
 */
@Composable
fun LoginScreenUI(
    authViewModel: AuthViewModel? = viewModel(),
    onGoogleSignInClick: (() -> Unit)? = null
) {
    // Local UI state
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var message by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current

    // Observe ViewModel LiveData when provided
    if (authViewModel != null) {
        val user by authViewModel.userLiveData.observeAsState()
        val error by authViewModel.errorLiveData.observeAsState()

        // react to login success
        LaunchedEffect(user) {
            if (user != null) {
                // Show a quick success message
                message = "Login Successful!"

                // Navigate to MainActivity
                val intent = Intent(context, com.example.kleenpride.ui.homescreen.MainActivity::class.java)
                context.startActivity(intent)

            }
        }

        // react to login errors
        LaunchedEffect(error) {
            error?.let {
                message = it
            }
        }
    }

    Surface(
        color = Color.Black,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Small heading
            Text(
                text = "Welcome Back",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
            // Main title
            Text(
                text = "Log into your Account",
                color = Color.White,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(25.dp))

            // Re-usable text fields (from ui.components)
            CustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) }
            )
            Spacer(modifier = Modifier.height(10.dp))

            CustomTextField(
                value = password,
                onValueChange = { password = it },
                label = "Password",
                leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
                isPassword = true
            )
            Spacer(modifier = Modifier.height(20.dp))

            CustomButton(
                text = "Login",
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        authViewModel?.login(email, password)
                    } else {
                        message = "Please enter email and password"
                    }
                },
                containerColor = Color.Green,
                contentColor = Color.Black,
            )

            // Forgot password small link - uses ViewModel.resetPassword if email provided
            TextButton(onClick = {
                if (email.isNotEmpty()) {
                    authViewModel?.resetPassword(email)
                    message = "Password reset email sent if account exists"
                } else {
                    message = "Enter your email to reset password"
                }
            }) {
                Text(
                    text = "Forgot Password?",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            // OR separator (re-usable component)
            OrSeparator()

            // Google Sign-In button (calls provided lambda to launch Google Sign-In)
            Button(
                onClick = { onGoogleSignInClick?.invoke() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth(0.7f),
                shape = RoundedCornerShape(30.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Google Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Continue with Google")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Feedback message (success or error)
            message?.let {
                Text(
                    text = it,
                    color = if (it.contains("Success", ignoreCase = true)) Color.Green else Color.Red,
                    modifier = Modifier.padding(top = 10.dp)
                )
            }
        }
    }
}

// Preview for quick layout check in Android Studio
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreenUI(authViewModel = null, onGoogleSignInClick = null)
}

