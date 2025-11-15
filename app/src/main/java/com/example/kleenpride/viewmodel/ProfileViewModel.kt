package com.example.kleenpride.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    // User data
    val fullName = mutableStateOf("")
    val email = mutableStateOf("")
    val profileImageUrl = mutableStateOf<String?>(null)
    val initials = mutableStateOf("U")

    // Preferences
    val enableNotifications = mutableStateOf(true)
    val receivePromotions = mutableStateOf(false)
    val receiveReminders = mutableStateOf(true)

    // Loading states
    val isLoading = mutableStateOf(false)
    val isUploadingImage = mutableStateOf(false)

    init {
        loadUserProfile()
        loadPreferences()
    }

    /**
     * Loads user profile data from Firestore.
     */
    private fun loadUserProfile() {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                val firstName = doc.getString("firstName") ?: ""
                val lastName = doc.getString("lastName") ?: ""
                fullName.value = "$firstName $lastName".trim()
                email.value = auth.currentUser?.email ?: ""
                profileImageUrl.value = doc.getString("profileImageUrl")

                // Calculate initials
                initials.value = if (firstName.isNotEmpty() && lastName.isNotEmpty()) {
                    "${firstName.first()}${lastName.first()}".uppercase()
                } else if (firstName.isNotEmpty()) {
                    firstName.take(2).uppercase()
                }else {
                    "U"
                }
            }
            .addOnFailureListener { e ->
                Log.e("ProfileViewModel", "Error loading profile", e)
            }
    }

    /**
     * Load user preferences from Firestore
     */
    private fun loadPreferences() {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                enableNotifications.value = doc.getBoolean("enableNotifications") ?: true
                receivePromotions.value = doc.getBoolean("receivePromotions") ?: false
                receiveReminders.value = doc.getBoolean("receiveReminders") ?: true
            }
            .addOnFailureListener { e ->
                Log.e("ProfileViewModel", "Error loading preferences", e)
            }
    }

    /**
     * Upload profile image to Firebase Storage
     */

    fun uploadProfileImage(
        uri: Uri,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: run {
            onError("User not authenticated")
            return
        }

        isUploadingImage.value = true

        // Create reference to storage location
        val storageRef = storage.reference
            .child("profile_images")
            .child("$uid.jpg")

        // Upload file
        storageRef.putFile(uri)
            .addOnSuccessListener {
                // Get download URL
                storageRef.downloadUrl
                    .addOnSuccessListener { downloadUri ->
                        val imageUrl = downloadUri.toString()

                        // Save URL to Firestore
                        firestore.collection("users").document(uid)
                            .update("profileImageUrl", imageUrl)
                            .addOnSuccessListener {
                                profileImageUrl.value = imageUrl
                                isUploadingImage.value = false
                                onSuccess(imageUrl)
                            }
                            .addOnFailureListener { e ->
                                Log.e("ProfileViewModel", "Error saving profile image URL", e)
                                isUploadingImage.value = false
                                onError("Failed to save image URL")
                            }
                    }
                    .addOnFailureListener { e ->
                        Log.e("ProfileViewModel", "Error getting download URL", e)
                        isUploadingImage.value = false
                        onError("Failed to get image URL")
                    }
            }
            .addOnFailureListener { e ->
                Log.e("ProfileViewModel", "Error uploading image", e)
                isUploadingImage.value = false
                onError("Failed to upload Image")
            }
    }

    /**
     * Save user preferences to Firestore
     */
    fun savePreferences(
        onSuccess: () -> Unit =  {},
        onError: (String) -> Unit = {}
    ) {
        val uid = auth.currentUser?.uid ?: run {
            onError("User not authenticated")
            return
        }

        isLoading.value = true

        firestore.collection("users").document(uid)
            .update(
                mapOf(
                    "enableNotifications" to enableNotifications.value,
                    "receivePromotions" to receivePromotions.value,
                    "receiveReminders" to receiveReminders.value
                )
            )
            .addOnSuccessListener {
                isLoading.value = false
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("ProfileViewModel", "Error saving preferences", e)
                isLoading.value = false
                onError("Failed to save preferences")
            }
    }

    /**
     * Sign out user
     */

    fun signOut() {
        auth.signOut()
    }
}