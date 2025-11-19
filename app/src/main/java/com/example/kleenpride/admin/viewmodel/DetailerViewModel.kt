package com.example.kleenpride.admin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kleenpride.admin.data.models.Detailer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

    private val _needsReLogin = MutableLiveData<Boolean>()
    val needsReLogin: LiveData<Boolean> = _needsReLogin

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
                        val joinDateTimestamp = doc.getTimestamp("joinDate")
                        val joinDateString = if (joinDateTimestamp != null) {
                            val date = joinDateTimestamp.toDate()
                            SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(date)
                        } else {
                            SimpleDateFormat("MMM yyyy", Locale.getDefault()).format(Date())
                        }

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
                            joinDate = joinDateString,
                            role = "DETAILER"
                        )
                    } catch (e: Exception) {
                        println("DEBUG: Error parsing document ${doc.id}: ${e.message}")
                        null
                    }
                }
                _detailers.value = detailersList
                _isLoading.value = false
                println("DEBUG: Successfully parsed ${detailersList.size} detailers")
            }
            .addOnFailureListener { e ->
                _error.value = e.message
                _isLoading.value = false
            }
    }

    /**
     * Create a new detailer account
     * WARNING: This will sign out the current admin user
     */
    fun createDetailer(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phoneNumber: String
    ) {
        _isLoading.value = true
        _createSuccess.value = false

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                val newUserId = authResult.user?.uid ?: return@addOnSuccessListener

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
                        // Sign out the newly created detailer
                        auth.signOut()

                        _createSuccess.value = true
                        _isLoading.value = false
                        _needsReLogin.value = true

                        println("DEBUG: Detailer created successfully")
                    }
                    .addOnFailureListener { e ->
                        _error.value = "Failed to create detailer profile: ${e.message}"
                        _isLoading.value = false

                        // Clean up auth account if Firestore creation failed
                        authResult.user?.delete()
                    }
            }
            .addOnFailureListener { e ->
                _error.value = "Failed to create account: ${e.message}"
                _isLoading.value = false
                println("DEBUG: Auth creation failed: ${e.message}")
            }
    }

    /**
     * Update detailer status (ACTIVE/INACTIVE)
     */
    fun updateDetailerStatus(detailerId: String, newStatus: String) {
        _isLoading.value = true

        firestore.collection("users").document(detailerId)
            .update("status", newStatus)
            .addOnSuccessListener {
                _isLoading.value = false
                loadDetailers()
            }
            .addOnFailureListener { e ->
                _error.value = e.message
                _isLoading.value = false
            }
    }

    /**
     * Actually delete a detailer from Firebase Auth and Firestore
     */
    fun deleteDetailer(detailerId: String) {
        _isLoading.value = true

        // First, delete from Firestore
        firestore.collection("users").document(detailerId)
            .delete()
            .addOnSuccessListener {
                _isLoading.value = false
                loadDetailers()
                println("DEBUG: Detailer deleted successfully")
            }
            .addOnFailureListener { e ->
                _error.value = "Failed to delete detailer: ${e.message}"
                _isLoading.value = false
            }
    }

    /**
     * Reset create success state
     */
    fun resetCreateSuccess() {
        _createSuccess.value = false
    }

    /**
     * Reset error state
     */
    fun resetError() {
        _error.value = null
    }

    /**
     * Reset re-login flag
     */
    fun resetReLoginFlag() {
        _needsReLogin.value = false
    }
}