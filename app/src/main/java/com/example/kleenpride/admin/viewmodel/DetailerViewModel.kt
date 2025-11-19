package com.example.kleenpride.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Detailer(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val rating: Float = 0f,
    val totalJobs: Int = 0,
    val earnings: Int = 0,
    val status: String = "ACTIVE",
    val joinDate: String = "",
    val role: String = "DETAILER"
)

class AdminDetailersViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _detailers = MutableLiveData<List<Detailer>>()
    val detailers: LiveData<List<Detailer>> = _detailers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _createSuccess = MutableLiveData<Boolean>()
    val createSuccess: LiveData<Boolean> = _createSuccess

    init {
        loadDetailers()
    }

    /**
     * Load all detailers from Firestore
     */
    fun loadDetailers() {
        _isLoading.value = true

        firestore.collection("users")
            .whereEqualTo("role", "DETAILER")
            .get()
            .addOnSuccessListener { snapshot ->
                val detailersList = snapshot.documents.mapNotNull { doc ->
                    try {
                        Detailer(
                            id = doc.id,
                            firstName = doc.getString("firstName") ?: "",
                            lastName = doc.getString("lastName") ?: "",
                            email = doc.getString("email") ?: "",
                            phoneNumber = doc.getString("phoneNumber") ?: "",
                            rating = doc.getDouble("rating")?.toFloat() ?: 0f,
                            totalJobs = doc.getLong("totalJobs")?.toInt() ?: 0,
                            earnings = doc.getLong("earnings")?.toInt() ?: 0,
                            status = doc.getString("status") ?: "ACTIVE",
                            joinDate = doc.getString("joinDate") ?: "",
                            role = "DETAILER"
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                _detailers.value = detailersList
                _isLoading.value = false
            }
            .addOnFailureListener { e ->
                _error.value = e.message
                _isLoading.value = false
            }
    }

    /**
     * Create a new detailer account (Admin only)
     * This creates the Firebase Auth account AND the Firestore document
     */
    fun createDetailer(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String,
        onSuccess: () -> Unit = {}
    ) {
        _isLoading.value = true

        // First, create Firebase Auth account
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val newUserId = authResult.user?.uid ?: return@addOnSuccessListener

                // Then create Firestore document with DETAILER role
                val detailerData = hashMapOf(
                    "firstName" to firstName,
                    "lastName" to lastName,
                    "email" to email,
                    "phoneNumber" to phoneNumber,
                    "role" to "DETAILER",
                    "status" to "ACTIVE",
                    "rating" to 0.0,
                    "totalJobs" to 0,
                    "earnings" to 0,
                    "joinDate" to com.google.firebase.Timestamp.now(),
                    "createdAt" to com.google.firebase.Timestamp.now()
                )

                firestore.collection("users").document(newUserId)
                    .set(detailerData)
                    .addOnSuccessListener {
                        _createSuccess.value = true
                        _isLoading.value = false
                        loadDetailers() // Refresh list
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        _error.value = "Failed to create detailer profile: ${e.message}"
                        _isLoading.value = false
                        // Delete the auth account if Firestore creation fails
                        authResult.user?.delete()
                    }
            }
            .addOnFailureListener { e ->
                _error.value = "Failed to create account: ${e.message}"
                _isLoading.value = false
            }
    }

    /**
     * Update detailer status (ACTIVE/INACTIVE)
     */
    fun updateDetailerStatus(detailerId: String, newStatus: String) {
        firestore.collection("users").document(detailerId)
            .update("status", newStatus)
            .addOnSuccessListener {
                loadDetailers() // Refresh list
            }
            .addOnFailureListener { e ->
                _error.value = e.message
            }
    }

    /**
     * Delete a detailer (soft delete by setting status to DELETED)
     */
    fun deleteDetailer(detailerId: String) {
        firestore.collection("users").document(detailerId)
            .update("status", "DELETED")
            .addOnSuccessListener {
                loadDetailers()
            }
            .addOnFailureListener { e ->
                _error.value = e.message
            }
    }
}